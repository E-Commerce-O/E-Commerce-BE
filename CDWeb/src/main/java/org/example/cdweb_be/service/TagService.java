package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.TagCreateRequest;
import org.example.cdweb_be.entity.Tag;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.TagMapper;
import org.example.cdweb_be.respository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TagService {
    TagRepository tagRepository;
    TagMapper tagMapper;

    public Tag addTag(TagCreateRequest request) {
        Optional<Tag> tagOptional = tagRepository.findById(request.getName());
        if (tagOptional.isPresent()) {
            throw new AppException(ErrorCode.TAG_EXISTED);
        } else {

            return tagRepository.save(tagMapper.toTag(request));
        }
    }

    public Tag getTagByName(String tagName) {
        Optional<Tag> tagOptional = tagRepository.findById(tagName);
        if (tagOptional.isPresent()) {
            return tagOptional.get();
        } else {
            throw new AppException(ErrorCode.NOT_FOUND);

        }
    }

    public List<Tag> getAll() {
        return tagRepository.findAll();
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
            throw new AppException(ErrorCode.NOT_FOUND);
        }
    }

    public void deleteTag(String tagName) {
        tagRepository.deleteById(tagName);
    }
}
