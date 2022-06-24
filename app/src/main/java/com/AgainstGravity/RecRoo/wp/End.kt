package com.AgainstGravity.RecRoo.wp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.AgainstGravity.RecRoo.R
import kotlinx.android.synthetic.main.activity_end.*

class End : AppCompatActivity() {
    val data = listOf(
        RVDataClass(R.drawable.pump,"Pumpkin", "On the vitamins and minerals side, it has an impressive amount of vitamin A, with 703 micrograms per cup. That’s 78 percent of the daily value (DV) of the vitamin, which supports immune health, vision, and cell growth, according to the National Institutes of Health (NIH)."),
        RVDataClass(R.drawable.brocc,"Broccoli", "Although you'd never guess it, broccoli has its origins in the wild mustard plant. It was bred by farmers over time to be the crunchy, green vegetable we know today -- and it's loaded with healthy nutrients."),
        RVDataClass(R.drawable.cabb,"Cabbage", "Half a cup of cooked cabbage has about a third the vitamin C you need for the day. It also gives you doses of fiber, folate, potassium, magnesium, vitamins A and K, and more."),
        RVDataClass(R.drawable.tom,"Tomato", "Although often considered a vegetable, the tomato is actually a fruit that belongs to the nightshade family, along with aubergines, peppers and potatoes. They come in a range of sizes and varieties from small cherry to large beefsteak. Traditionally, red in colour, you can also get tomatoes in yellow, green, purple, orange and variegated colours."),
        RVDataClass(R.drawable.apple,"Apple", "Not only do apples taste delicious on their own or when added to dishes but they come loaded with health benefits. “Apples have been linked to numerous health benefits, including improved gut health and reduced risk of stroke, high blood pressure, diabetes, heart disease, obesity, and some cancers,"),
        RVDataClass(R.drawable.straw,"Strawberry", "The heart-shaped silhouette of the strawberry is the first clue that this fruit is good for you. These potent little packages protect your heart, increase HDL (good) cholesterol, lower your blood pressure, and guard against cancer."),
        RVDataClass(R.drawable.blue,"Blueberry", "Blueberries not only make a great snack but are a healthy addition to most people's diet. These small fruits pack a powerful antioxidant punch, help lower blood pressure, and improve insulin sensitivity. "),
        RVDataClass(R.drawable.ban,"Banana", "Bananas have a distinct shape and a firm but creamy flesh inside a thick, inedible peel. While people think of bananas as having yellow skin, their colour changes from green (under ripe) to yellow (ripe) to brown (overripe)."),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RVAdapter(data)
    }
}