package com.yangming.boot.demo.system.mongodb;

import com.yangming.boot.demo.system.model.MemberReadHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MemberReadHistoryRepository extends MongoRepository<MemberReadHistory,String> {

    List<MemberReadHistory> findByMemberIdOrderByCreateTimeDesc(Long memberId);
}
