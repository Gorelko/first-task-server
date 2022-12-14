package com.balinasoft.firsttask.controller.api1;

import com.balinasoft.firsttask.dto.ImageDtoIn;
import com.balinasoft.firsttask.dto.ImageDtoOut;
import com.balinasoft.firsttask.dto.ResponseDto;
import com.balinasoft.firsttask.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import java.util.Set;

import static com.balinasoft.firsttask.system.StaticWrapper.wrap;

@RestController
@RequestMapping("/api/image")
@Api(tags = "Images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Upload image", response = ImageDtoOut.class)
    @ApiResponses({
            @ApiResponse(code = 400, message = "bad-image"),
            @ApiResponse(code = 500, message = "file-upload-error")
    })
    public ResponseDto uploadImage(@RequestBody @Valid ImageDtoIn imageDtoIn) {
        return wrap(imageService.uploadImage(imageDtoIn));
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Upload image", response = ImageDtoOut.class)
    public ResponseDto deleteImage(@PathVariable int id) {
        imageService.deleteImage(id);
        return wrap();
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Upload image", response = ImageDtoOut.class, responseContainer = "List")
    public ResponseDto getImages(@RequestParam int page) {
        return wrap(imageService.getImages(page));
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "by-categories", method = RequestMethod.GET)
    @ApiOperation(value = "Upload image")
    public ResponseDto getImagesByCategories(@RequestParam Set<String> categories) {
        return wrap(imageService.getImagesByCategories(categories));
    }
}
