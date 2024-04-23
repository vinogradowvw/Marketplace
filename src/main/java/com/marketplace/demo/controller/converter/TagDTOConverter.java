package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.TagDTO;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.service.PostService.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TagDTOConverter implements DTOConverter<TagDTO, Tag> {

    private final PostService postService;

    @Override
    public TagDTO toDTO(Tag tag) {
        List<Long> postsId = new ArrayList<>();

        Optional<List<Post>> optPosts = Optional.ofNullable(tag.getPosts());

        if (optPosts.isPresent()) {
            for (Post post : tag.getPosts()){
                postsId.add(post.getID());
            }
        }

        return new TagDTO(tag.getID(), tag.getName(), postsId);
    }

    @Override
    public Tag toEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.id());
        tag.setName(tagDTO.name());

        List<Post> posts = new ArrayList<>();

        Optional<List<Long>> postList = Optional.ofNullable(tagDTO.posts());

        if (postList.isPresent()){
            for (Long id : postList.get()){
                Optional<Post> optPost = postService.readById(id);
                optPost.ifPresent(posts::add);
            }
        }

        tag.setPosts(posts);

        return tag;
    }
}
