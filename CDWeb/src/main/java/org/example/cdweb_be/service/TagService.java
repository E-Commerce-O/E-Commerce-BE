package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.dto.request.TagCreateRequest;
import org.example.cdweb_be.dto.response.PagingResponse;
import org.example.cdweb_be.entity.Tag;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.TagMapper;
import org.example.cdweb_be.respository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TagService {
    MessageProvider messageProvider;
    TagRepository tagRepository;
    TagMapper tagMapper;

    public Tag addTag(TagCreateRequest request) {
        Optional<Tag> tagOptional = tagRepository.findById(request.getName());
        if (tagOptional.isPresent()) {
            throw new AppException(messageProvider,ErrorCode.TAG_EXISTED);
        } else {

            return tagRepository.save(tagMapper.toTag(request));
        }
    }

    public Tag getTagByName(String tagName) {
        Optional<Tag> tagOptional = tagRepository.findById(tagName);
        if (tagOptional.isPresent()) {
            return tagOptional.get();
        } else {
            throw new AppException(messageProvider,ErrorCode.NOT_FOUND);

        }
    }

    public PagingResponse getAll(int page, int size) {
        Page<Tag> tags = tagRepository.findAll(PageRequest.of(page-1, size));
        return PagingResponse.<Tag>builder()
                .page(page)
                .size(size)
                .totalItem(tagRepository.count())
                .data(tags.stream().toList())
                .build();
    }
    public List<Tag> getAll() {
        List<Tag> tags = tagRepository.findAll();
        return tags;
    }

    public List<Tag> getAllByid(List<String> tagNames) {
        return tagRepository.findAllById(tagNames);
    }

    public Tag updateTag(TagCreateRequest request) {
        Optional<Tag> tagOptional = tagRepository.findById(request.getName());
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            tag.setDescription(request.getDescription());
            return tagRepository.save(tag);
        } else {
            throw new AppException(messageProvider,ErrorCode.NOT_FOUND);
        }
    }

    public String deleteTag(String tagName) {
        tagRepository.deleteById(tagName);
        return messageProvider.getMessage("delete.tag");
    }
}
