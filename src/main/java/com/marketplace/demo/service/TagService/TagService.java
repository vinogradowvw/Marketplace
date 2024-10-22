package com.marketplace.demo.service.TagService;

import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.persistance.PostRepository;
import com.marketplace.demo.persistance.TagRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class TagService extends CrudServiceImpl<Tag, Long> implements TagServiceInterface {

    private TagRepository tagRepository;
    private PostRepository postRepository;

    @Override
    public void deleteById(Long id) throws IllegalArgumentException {
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("Tag with id " + id + " not found");
        }

        Tag tag = tagRepository.findById(id).get();

        for (Post post : tag.getPosts()) {
            post.getTags().remove(tag);
            postRepository.save(post);
        }

        tagRepository.deleteById(id);
    }

    public List<Tag> getEntitiesbyIds(List<Long> ids) {

        List<Tag> tags = new ArrayList<>();

        for (Long id : ids) {
            Optional<Tag> tag = tagRepository.findById(id);

            tag.ifPresent(tags::add);
        }

        return tags;
    }

    @Override
    protected CrudRepository<Tag, Long> getRepository() {
        return tagRepository;
    }
}
