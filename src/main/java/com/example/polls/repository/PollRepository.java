package com.example.polls.repository;

import com.example.polls.model.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by rajeevkumarsingh on 20/11/17.
 */
@Repository
public interface PollRepository extends JpaRepository<Poll, String> {

    Optional<Poll> findById(String pollId);

    Page<Poll> findByCreatedBy(String userId, Pageable pageable);

    long countByCreatedBy(String userId);

    List<Poll> findByIdIn(List<String> pollIds);

    List<Poll> findByIdIn(List<String> pollIds, Sort sort);
}
