//package com.zhiyixi.esdao;
//
//import com.yupi.springbootinit.model.dto.post.PostEsDTO;
//import java.util.List;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
///**
// * 帖子 ES 操作
// * @author <a href="https://github.com/Agan-ippe">知莫</a>
// */
//public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
//
//    List<PostEsDTO> findByUserId(Long userId);
//}