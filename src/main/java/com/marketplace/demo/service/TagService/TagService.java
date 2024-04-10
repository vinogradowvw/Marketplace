package com.marketplace.demo.service.TagService;

import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.persistance.TagRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TagService extends CrudServiceImpl<Tag, Long> implements TagServiceInterface {

    private TagRepository tagRepository;

    @Override
    protected CrudRepository<Tag, Long> getRepository() {
        return tagRepository;
    }
}
