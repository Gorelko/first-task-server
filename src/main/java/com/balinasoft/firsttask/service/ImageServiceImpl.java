package com.balinasoft.firsttask.service;

import com.balinasoft.firsttask.domain.Category;
import com.balinasoft.firsttask.domain.Image;

import com.balinasoft.firsttask.domain.User;
import com.balinasoft.firsttask.dto.ImageDtoIn;
import com.balinasoft.firsttask.dto.ImageDtoOut;
import com.balinasoft.firsttask.repository.CategoryRepository;
import com.balinasoft.firsttask.repository.ImageRepository;
import com.balinasoft.firsttask.repository.UserRepository;
import com.balinasoft.firsttask.system.error.ApiAssert;
import com.balinasoft.firsttask.util.StringGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

import static com.balinasoft.firsttask.util.SecurityContextHolderWrapper.currentUserId;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${project.image-folder}")
    private String imageFolder;

    @Value("${project.url}")
    private String url;

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public ImageDtoOut uploadImage(ImageDtoIn imageDtoIn) {
        String fileName;
        try {
            fileName = saveImage(imageDtoIn.getBase64Image());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        User user = userRepository.findOne(currentUserId());
        Image image = new Image();
        image.setUrl(fileName);
        image.setUser(user);
        image.setLat(imageDtoIn.getLat());
        image.setLng(imageDtoIn.getLng());
        image.setDate(imageDtoIn.getDate());
        Category category = categoryRepository.findByName(imageDtoIn.getCategory());

        if (category == null) {
            throw new EntityNotFoundException("Категория под именем '" + imageDtoIn.getCategory() + "' не существует в базе данных!");
        } else {
            image.setCategory(category);
        }

        image = imageRepository.save(image);
        return toDto(image);
    }

    @Override
    public void deleteImage(int id) {
        Image image = imageRepository.findOne(id);
        ApiAssert.notFound(image == null);
        ApiAssert.forbidden(image.getUser().getId() != currentUserId());
        try {
            Files.delete(Paths.get(getFullPath(image.getUrl())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageRepository.delete(image);
    }

    @Override
    public List<ImageDtoOut> getImages(int page) {
        List<Image> images = imageRepository.findByUser(currentUserId(), new PageRequest(page, 20));
        return images.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ImageDtoOut> getImagesByCategories(Set<String> categories) {
        for (String category : categories) {
            Category existCategory = categoryRepository.findByName(category);
            if (existCategory == null) {
                throw new EntityNotFoundException("Категория под именем '" + category + "' не существует в базе данных!");
            }
        }

        List<Image> imageList = imageRepository.findByCategoryNameIn(categories);
        return imageList.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ImageDtoOut toDto(Image image) {
        return new ImageDtoOut(image.getId(),
                url + "/images/" + image.getUrl(),
                image.getDate(),
                image.getLat(),
                image.getLng(),
                image.getCategory().getName());
    }

    private String saveImage(String base64Image) throws IOException {
        byte[] bytes = Base64.decodeBase64(base64Image);
        String extension = checkImage(bytes);

        String fileName = generateUniqueFileName("uploaded", extension);
        Path destinationFile = Paths.get(getFullPath(fileName));

        Files.write(destinationFile, bytes, StandardOpenOption.CREATE);
        return fileName;
    }

    private String checkImage(byte[] bytes) throws IOException {
        int maxSize = 1280;
        byte[] jpegMagicNumber = new byte[]{(byte) 0xff, (byte) 0xd8, (byte) 0xff, (byte) 0xe0};
        byte[] pngMagicNumber = new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47};

        byte[] magicNumber = Arrays.copyOf(bytes, 4);

        String extension = "";
        if (Arrays.equals(jpegMagicNumber, magicNumber)) {
            extension = "jpg";
        } else if (Arrays.equals(pngMagicNumber, magicNumber)) {
            extension = "png";
        } else {
            //noinspection ConstantConditions
            ApiAssert.badRequest(true, "bad-image");
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(bis);
        } catch (IIOException e) {
            //noinspection ConstantConditions
            ApiAssert.badRequest(true, "bad-image");
        }
        ApiAssert.badRequest(bufferedImage == null, "bad-image");
        ApiAssert.badRequest(bufferedImage.getWidth() > maxSize || bufferedImage.getHeight() > maxSize,
                "big-image");
        return extension;
    }


    private String generateUniqueFileName(String folder, String extension) throws IOException {
        String fileName;
        do {
            fileName = folder + generateRandomFileName(extension);
        }
        while (Files.exists(Paths.get(getFullPath(fileName))));

        createFolders(fileName);

        return fileName;
    }

    private String generateRandomFileName(String extension) {
        Calendar c = Calendar.getInstance();
        return "/" +
                c.get(Calendar.YEAR) +
                "/" +
                c.get(Calendar.MONTH) +
                "/" +
                c.get(Calendar.DAY_OF_MONTH) +
                "/" +
                StringGenerator.generate(32) +
                "." +
                extension;
    }

    private String getFullPath(String fileName) {
        return imageFolder + "/" + fileName;
    }

    private void createFolders(String fileName) throws IOException {
        String onlyFolder = fileName.substring(0, fileName.lastIndexOf('/'));
        Files.createDirectories(Paths.get(getFullPath(onlyFolder)));
    }
}
