package com.xyauto.qa.service

import com.xyauto.qa.mapper.qa.AnswerMapper
import com.xyauto.qa.mapper.qa.QuestionMapper
import com.xyauto.qa.model.Answer
import com.xyauto.qa.model.Question
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by shiqm on 2017-09-18.
 */

@Service
class AnswerService {

    @Autowired
    lateinit var answerMapper: AnswerMapper


    @Autowired
    lateinit var questionMapper: QuestionMapper


    fun getByThird(third_id: String, source: Int): Answer? {
        return answerMapper.getByThird(third_id, source)
    }

    @Transactional
    fun add(answer: Answer){
        answerMapper.add(answer)
        val question: Question = questionMapper.selectOne(Question(question_id = answer.question_id))
        //处理问题第一次回复时间
        if (question.answer_count == 0) {
            val unixTime = Math.ceil(System.currentTimeMillis().toDouble() / 1000L).toInt()
            questionMapper.updateByPrimaryKeySelective(Question(question_id = answer.question_id, first_answerd_at = unixTime))
        }
        //处理问题回复数
        questionMapper.incAnswerCount(answer.question_id)
        //处理问题总回答人数
        questionMapper.recountAnswerUserCount(answer.question_id)
        //处理回复人回答数
        questionMapper.recountAnswerCount(answer.uid)
    }

}