package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.TagService.TagService;
import com.marketplace.demo.service.UserService.UserService;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostDTOConverter implements DTOConverter<PostDTO, Post> {

    private final UserService userService;
    private final TagService tagService;
    private final ImageService imageService;
    private final ProductService productService;


    @Override
    public PostDTO toDTO(Post post) {

        Optional<List<Product>> optProductsInPost = Optional.ofNullable(post.getProductsInPost());
        ArrayList<Long> productInPostIds = new ArrayList<>();
        if (optProductsInPost.isPresent()) {
            for (Product productInPost : optProductsInPost.get()) {
                productInPostIds.add(productInPost.getID());
            }
        }

        Optional<List<User>> optLikedUsers = Optional.ofNullable(post.getLikedUsers());
        ArrayList<Long> likedUserIds = new ArrayList<>();
        if (optLikedUsers.isPresent()) {
            for (User likedUser : optLikedUsers.get()) {
                likedUserIds.add(likedUser.getID());
            }
        }

        Optional<List<Tag>> optTags = Optional.ofNullable(post.getTags());
        ArrayList<Long> tagIds = new ArrayList<>();
        if (optLikedUsers.isPresent()) {
            for (Tag tag : optTags.get()) {
                tagIds.add(tag.getID());
            }
        }

        Optional<User> optUser = Optional.ofNullable(post.getUser());
        Long userId = null;
        if (optUser.isPresent()) {
            userId = optUser.get().getID();
        }

        Optional<List<Image>> optImages = Optional.ofNullable(post.getImages());
        ArrayList<Long> imageIds = new ArrayList<>();
        if (optImages.isPresent()) {
            for (Image image : optImages.get()){
                imageIds.add(image.getID());
            }
        }

        return new PostDTO(post.getID(), post.getName(), post.getDescription(), 
                            productInPostIds, userId, likedUserIds, tagIds, imageIds);
    }

    @Override
    public Post toEntity(PostDTO postDTO) {
        
        Post post = new Post();
        
        post.setId(postDTO.id());
        post.setName(postDTO.name());

        Optional<String> optDescription = Optional.ofNullable(postDTO.description());
        if (optDescription.isPresent()) {
            post.setDescription(optDescription.get());
        }

        Optional<List<Long>> optProductIds = Optional.ofNullable(postDTO.product());
        List<Product> products = new ArrayList<>();

        if (optProductIds.isPresent()) {
            for (Long productId : optProductIds.get()) {
                Optional<Product> product = Optional.empty();
                product = productService.readById(productId);
                if (product.isPresent()) {
                    products.add(product.get());
                }
            }
        }

        post.setProductsInPost(products);


        Optional<Long> optUserId = Optional.ofNullable(postDTO.user());
        Optional<User> user  = Optional.empty();

        if (optUserId.isPresent()) {
            user = userService.readById(optUserId.get());
        }
        
        if (user.isPresent()) {
            post.setUser(user.get());
        }
        else {
            post.setUser(null);
        }

        Optional<List<Long>> optLikedUserIds = Optional.ofNullable(postDTO.likedUsers());
        List<User> likedUsers = new ArrayList<>();

        if (optLikedUserIds.isPresent()){
            for (Long userId : optLikedUserIds.get()) {
                Optional<User> likedUser = Optional.empty();
                likedUser = userService.readById(userId);
                if (likedUser.isPresent()) {
                    likedUsers.add(likedUser.get());
                }
            }
        }

        post.setLikedUsers(likedUsers);


        Optional<List<Long>> optTagIds = Optional.ofNullable(postDTO.tags());
        List<Tag> tags = new ArrayList<>();

        if (optTagIds.isPresent()) {
            for (Long tagId : optTagIds.get()) {
                Optional<Tag> tag = Optional.empty();
                tag = tagService.readById(tagId);
                if (tag.isPresent()) {
                    tags.add(tag.get());
                }
            }
        }

        post.setTags(tags);


        Optional<List<Long>> optImageIds = Optional.ofNullable(postDTO.images());
        List<Image> images = new ArrayList<>();

        if (optImageIds.isPresent()) {
            for (Long imageId : optImageIds.get()) {

                Optional<Image> image = Optional.empty();
                image = imageService.readById(imageId);
                if (image.isPresent()) {
                    images.add(image.get());
                }
            }
        }

        post.setImages(images);

        return post;
    }
}
