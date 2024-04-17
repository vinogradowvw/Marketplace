package com.marketplace.demo.service.TagService;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.persistance.PostRepository;
import com.marketplace.demo.persistance.TagRepository;
import com.marketplace.demo.service.CrudServiceImpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class TagService extends CrudServiceImpl<Tag, Long> implements TagServiceInterface {

    TagRepository tagRepository;
    PostRepository postRepository;

    @Override
    protected CrudRepository<Tag, Long> getRepository() {
        return tagRepository;
    }

}
