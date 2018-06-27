package com.xyauto.qa.service

//import com.xyauto.qa.mapper.qa.CategoryMapper
import com.xyauto.qa.mapper.qa.CategoryMapper
import com.xyauto.qa.mapper.qa.QuestionMapper
import com.xyauto.qa.mapper.qa.UserMapper
import com.xyauto.qa.model.Question
import com.xyauto.qa.util.MD5
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by shiqm on 2017-09-15.
 */
@Service
class QuestionService {

    @Autowired
    lateinit var questionMapper: QuestionMapper

    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var categoryMapper: CategoryMapper

    fun getLastestThirdId(source: Int): String = questionMapper.getLastestThirdId(source)

    fun getByThird(third_id: String, source: Int): Question? = questionMapper.getByThird(third_id, source)

    @Transactional
    fun add(question: Question): Question? {
        return try {
            //设置question key
            val key = MD5.shortUrl(question.question_id.toString())
            question.key = key ?: ""
            //写入问题
            questionMapper.add(question)
            //处理用户问题数
            userMapper.recountQuestionCount(question.uid)
            //处理分类问题数
            categoryMapper.incQuestionCount(question.category_id.toInt())
            question
        } catch (e: Exception) {
            null
        }
    }

    fun incAnswerCount(question_id: Long){
        questionMapper.incAnswerCount(question_id)
    }

    fun recountAnswerUserCount(question_id: Long){
        questionMapper.recountAnswerUserCount(question_id)
    }

    fun recountAnswerCount(uid: Long){
        questionMapper.recountAnswerCount(uid)
    }



}