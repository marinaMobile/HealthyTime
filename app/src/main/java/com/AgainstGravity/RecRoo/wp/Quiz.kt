package com.AgainstGravity.RecRoo.wp

object Quiz {

    fun getQues(): ArrayList<Question> {

        val quesList = ArrayList<Question>()

        val ques1 = Question(1, "How many times a week do you eat out?",
            "0-2", "3-5", "More than 5 times", "Next question")

        quesList.add(ques1)

        val ques2 = Question(2, "Do you exercise?",
            "I have a strict regime that I follow", "Once or twice a week", "A few times a month", "I hardly exercise")

        quesList.add(ques2)

        val ques3 = Question(3, "What do you do in your free time?",
            "Outdoor activities", "Party/ eating out", "Television/surfing the Internet", "What free time?")

        quesList.add(ques3)

        return quesList
    }
}