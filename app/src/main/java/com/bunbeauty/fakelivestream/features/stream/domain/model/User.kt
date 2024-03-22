package com.bunbeauty.fakelivestream.features.stream.domain.model

import kotlin.random.Random

data class User(
    val name: String,
    val surname: String,
) {

    val age: Int by lazy {
        Random.nextInt(16, 70)
    }

    companion object {
        val list = listOf(
            User(name = "emma", surname = "johnson"),
            User(name = "liam", surname = "martinez"),
            User(name = "olivia", surname = "brown"),
            User(name = "noah", surname = "garcia"),
            User(name = "ava", surname = "robinson"),
            User(name = "william", surname = "anderson"),
            User(name = "sophia", surname = "taylor"),
            User(name = "james", surname = "hernandez"),
            User(name = "isabella", surname = "white"),
            User(name = "oliver", surname = "lewis"),
            User(name = "mia", surname = "martin"),
            User(name = "benjamin", surname = "thompson"),
            User(name = "charlotte", surname = "perez"),
            User(name = "elijah", surname = "rodriguez"),
            User(name = "amelia", surname = "wilson"),
            User(name = "henry", surname = "lopez"),
            User(name = "harper", surname = "harris"),
            User(name = "alexander", surname = "clark"),
            User(name = "evelyn", surname = "lewis"),
            User(name = "michael", surname = "hughes"),
            User(name = "ella", surname = "turner"),
            User(name = "jacob", surname = "torres"),
            User(name = "aubrey", surname = "parker"),
            User(name = "emily", surname = "collins"),
            User(name = "ethan", surname = "murphy"),
            User(name = "ava", surname = "reed"),
            User(name = "mason", surname = "cook"),
            User(name = "sofia", surname = "bailey"),
            User(name = "logan", surname = "rivera"),
            User(name = "madison", surname = "ward"),
            User(name = "sebastian", surname = "cox"),
            User(name = "chloe", surname = "graham"),
            User(name = "daniel", surname = "sullivan"),
            User(name = "emma", surname = "thomas"),
            User(name = "carter", surname = "price"),
            User(name = "abigail", surname = "powell"),
            User(name = "jack", surname = "peterson"),
            User(name = "ava", surname = "richardson"),
            User(name = "charlotte", surname = "wood"),
            User(name = "luke", surname = "ward"),
            User(name = "victoria", surname = "nelson"),
            User(name = "joseph", surname = "cooper"),
            User(name = "grace", surname = "perry"),
            User(name = "william", surname = "james"),
            User(name = "hannah", surname = "bell"),
            User(name = "jayden", surname = "murphy"),
            User(name = "amelia", surname = "king"),
            User(name = "owen", surname = "butler"),
            User(name = "zoey", surname = "ross"),
            User(name = "luke", surname = "harrison"),
            User(name = "nora", surname = "evans"),
            User(name = "matthew", surname = "griffin"),
            User(name = "lily", surname = "watson"),
            User(name = "david", surname = "barnes"),
            User(name = "samantha", surname = "morales"),
            User(name = "connor", surname = "simmons"),
            User(name = "aubrey", surname = "lee"),
            User(name = "addison", surname = "lopez"),
            User(name = "john", surname = "baker"),
            User(name = "riley", surname = "rossi"),
            User(name = "julia", surname = "graham"),
            User(name = "ryan", surname = "stewart"),
            User(name = "emily", surname = "long"),
            User(name = "luca", surname = "jackson"),
            User(name = "hannah", surname = "price"),
            User(name = "aiden", surname = "scott"),
            User(name = "madeline", surname = "young"),
            User(name = "nathan", surname = "diaz"),
            User(name = "ella", surname = "morris"),
            User(name = "jackson", surname = "watson"),
            User(name = "isabelle", surname = "hughes"),
            User(name = "jacob", surname = "wilson"),
            User(name = "scarlett", surname = "roberts"),
            User(name = "christopher", surname = "wood"),
            User(name = "aubrey", surname = "lopez"),
            User(name = "avery", surname = "rogers"),
            User(name = "maya", surname = "hill"),
            User(name = "wyatt", surname = "gomez"),
            User(name = "claire", surname = "carter"),
            User(name = "henry", surname = "adams"),
            User(name = "brooklyn", surname = "fernandez"),
            User(name = "leo", surname = "hall"),
            User(name = "grace", surname = "bailey"),
            User(name = "jacob", surname = "cruz"),
            User(name = "violet", surname = "perkins"),
            User(name = "luke", surname = "cooper"),
            User(name = "zoey", surname = "stewart"),
            User(name = "christian", surname = "graham"),
            User(name = "mia", surname = "ortiz"),
            User(name = "wyatt", surname = "phillips"),
            User(name = "avery", surname = "robinson"),
            User(name = "hazel", surname = "butler"),
            User(name = "liam", surname = "stewart"),
            User(name = "madison", surname = "martinez"),
            User(name = "emma", surname = "turner"),
            User(name = "nathan", surname = "scott"),
            User(name = "lily", surname = "long"),
            User(name = "logan", surname = "kelly"),
            User(name = "amelia", surname = "mitchell"),
            User(name = "daniel", surname = "lopez"),
            User(name = "harper", surname = "ross"),
            User(name = "david", surname = "perez"),
            User(name = "ava", surname = "griffin"),
            User(name = "joseph", surname = "ortiz"),
            User(name = "grace", surname = "hall"),
            User(name = "jackson", surname = "clark"),
            User(name = "evelyn", surname = "young"),
            User(name = "jacob", surname = "diaz"),
            User(name = "charlotte", surname = "ward"),
            User(name = "liam", surname = "ramirez"),
            User(name = "mia", surname = "edwards"),
            User(name = "carter", surname = "parker"),
            User(name = "aubrey", surname = "king"),
            User(name = "michael", surname = "lewis"),
            User(name = "sophia", surname = "turner"),
            User(name = "owen", surname = "foster"),
            User(name = "riley", surname = "graham"),
            User(name = "claire", surname = "perez"),
            User(name = "john", surname = "barnes"),
            User(name = "emily", surname = "butler"),
            User(name = "dylan", surname = "scott"),
            User(name = "chloe", surname = "taylor"),
            User(name = "matthew", surname = "young"),
            User(name = "sophia", surname = "stewart"),
            User(name = "ryan", surname = "rogers"),
            User(name = "emma", surname = "jones"),
            User(name = "liam", surname = "gomez"),
            User(name = "olivia", surname = "miller"),
            User(name = "noah", surname = "davis"),
            User(name = "ava", surname = "ross"),
            User(name = "william", surname = "mitchell"),
            User(name = "isabella", surname = "johnson"),
            User(name = "oliver", surname = "hall"),
            User(name = "mia", surname = "phillips"),
            User(name = "benjamin", surname = "foster"),
            User(name = "charlotte", surname = "scott"),
            User(name = "elijah", surname = "edwards"),
            User(name = "amelia", surname = "martin"),
            User(name = "henry", surname = "collins"),
            User(name = "harper", surname = "lopez"),
            User(name = "alexander", surname = "murphy"),
            User(name = "evelyn", surname = "rogers"),
            User(name = "michael", surname = "hill"),
            User(name = "ella", surname = "morales"),
            User(name = "jacob", surname = "rodriguez"),
            User(name = "aubrey", surname = "garcia"),
            User(name = "emily", surname = "hernandez"),
            User(name = "ethan", surname = "ramirez"),
            User(name = "ava", surname = "perez"),
            User(name = "mason", surname = "nelson"),
            User(name = "sofia", surname = "wood"),
            User(name = "logan", surname = "stewart"),
            User(name = "madison", surname = "torres"),
            User(name = "sebastian", surname = "rodriguez"),
            User(name = "chloe", surname = "murphy"),
            User(name = "daniel", surname = "campbell"),
            User(name = "emma", surname = "hall"),
            User(name = "carter", surname = "morgan"),
            User(name = "abigail", surname = "miller"),
            User(name = "jack", surname = "reed"),
            User(name = "ava", surname = "martinez"),
            User(name = "charlotte", surname = "young"),
            User(name = "luke", surname = "mitchell"),
            User(name = "victoria", surname = "carter"),
            User(name = "joseph", surname = "hughes"),
            User(name = "grace", surname = "martinez"),
            User(name = "william", surname = "hernandez"),
            User(name = "hannah", surname = "lopez"),
            User(name = "jayden", surname = "scott"),
            User(name = "amelia", surname = "butler"),
            User(name = "owen", surname = "taylor"),
            User(name = "zoey", surname = "james"),
            User(name = "luke", surname = "kelly"),
            User(name = "nora", surname = "mitchell"),
            User(name = "matthew", surname = "graham"),
            User(name = "lily", surname = "bennett"),
            User(name = "david", surname = "perry"),
            User(name = "samantha", surname = "morris"),
            User(name = "connor", surname = "martinez"),
            User(name = "aubrey", surname = "thomas"),
            User(name = "addison", surname = "rodriguez"),
            User(name = "john", surname = "adams"),
            User(name = "riley", surname = "wood"),
            User(name = "julia", surname = "gomez"),
            User(name = "ryan", surname = "baker"),
            User(name = "emily", surname = "lopez"),
            User(name = "luca", surname = "perez"),
            User(name = "hannah", surname = "nelson"),
            User(name = "aiden", surname = "collins"),
            User(name = "madeline", surname = "brown"),
            User(name = "nathan", surname = "diaz"),
            User(name = "ella", surname = "foster"),
            User(name = "jackson", surname = "garcia"),
            User(name = "isabelle", surname = "ward"),
            User(name = "jacob", surname = "cooper"),
            User(name = "scarlett", surname = "hill"),
            User(name = "christopher", surname = "sullivan"),
            User(name = "aubrey", surname = "gomez"),
            User(name = "avery", surname = "hughes"),
            User(name = "maya", surname = "phillips"),
            User(name = "wyatt", surname = "ross"),
            User(name = "claire", surname = "miller"),
            User(name = "henry", surname = "lewis"),
            User(name = "brooklyn", surname = "thompson"),
            User(name = "leo", surname = "torres"),
            User(name = "grace", surname = "parker"),
            User(name = "jacob", surname = "stewart"),
            User(name = "violet", surname = "hernandez"),
            User(name = "luke", surname = "gonzalez"),
            User(name = "zoey", surname = "robinson"),
            User(name = "christian", surname = "kelly"),
            User(name = "mia", surname = "cooper"),
            User(name = "wyatt", surname = "martin"),
            User(name = "avery", surname = "price"),
            User(name = "hazel", surname = "miller"),
            User(name = "liam", surname = "young"),
            User(name = "madison", surname = "barnes"),
            User(name = "emma", surname = "rogers"),
            User(name = "nathan", surname = "murphy"),
            User(name = "lily", surname = "gonzalez"),
            User(name = "logan", surname = "reed"),
            User(name = "amelia", surname = "taylor"),
            User(name = "daniel", surname = "scott"),
            User(name = "harper", surname = "lopez"),
            User(name = "david", surname = "hill"),
            User(name = "ava", surname = "thomas"),
            User(name = "joseph", surname = "mitchell"),
            User(name = "grace", surname = "lee"),
            User(name = "jackson", surname = "james"),
            User(name = "evelyn", surname = "parker"),
            User(name = "jacob", surname = "phillips"),
            User(name = "aubrey", surname = "ward"),
            User(name = "emily", surname = "collins"),
            User(name = "ethan", surname = "rivera"),
            User(name = "ava", surname = "lopez"),
            User(name = "mason", surname = "murphy"),
            User(name = "sofia", surname = "morales"),
            User(name = "logan", surname = "martin"),
            User(name = "madison", surname = "thompson"),
            User(name = "sebastian", surname = "garcia"),
            User(name = "chloe", surname = "wilson"),
            User(name = "daniel", surname = "jackson"),
            User(name = "emma", surname = "rodriguez"),
            User(name = "carter", surname = "hughes"),
            User(name = "abigail", surname = "brown"),
            User(name = "jack", surname = "clark"),
            User(name = "ava", surname = "sullivan"),
            User(name = "charlotte", surname = "perry"),
            User(name = "luke", surname = "bell"),
            User(name = "victoria", surname = "gonzalez"),
            User(name = "joseph", surname = "campbell"),
            User(name = "grace", surname = "mitchell"),
            User(name = "william", surname = "martin"),
            User(name = "hannah", surname = "baker"),
            User(name = "jayden", surname = "ramirez"),
            User(name = "amelia", surname = "miller"),
            User(name = "owen", surname = "wilson"),
            User(name = "zoey", surname = "cooper"),
            User(name = "luke", surname = "griffin"),
            User(name = "nora", surname = "collins"),
            User(name = "matthew", surname = "richardson"),
            User(name = "lily", surname = "mitchell"),
            User(name = "david", surname = "hughes"),
            User(name = "samantha", surname = "stewart"),
            User(name = "connor", surname = "perez"),
            User(name = "aubrey", surname = "thompson"),
            User(name = "addison", surname = "jones"),
            User(name = "john", surname = "rodriguez"),
            User(name = "riley", surname = "cooper"),
            User(name = "julia", surname = "turner"),
            User(name = "ryan", surname = "robinson"),
            User(name = "emily", surname = "bennett"),
            User(name = "luca", surname = "gomez"),
            User(name = "hannah", surname = "ramirez"),
            User(name = "aiden", surname = "martin"),
            User(name = "madeline", surname = "gonzalez"),
            User(name = "nathan", surname = "edwards"),
            User(name = "ella", surname = "foster"),
            User(name = "jackson", surname = "parker"),
            User(name = "isabelle", surname = "taylor"),
            User(name = "jacob", surname = "lopez"),
            User(name = "scarlett", surname = "martinez"),
            User(name = "christopher", surname = "phillips"),
            User(name = "aubrey", surname = "miller"),
            User(name = "avery", surname = "rodriguez"),
            User(name = "maya", surname = "hernandez"),
            User(name = "wyatt", surname = "garcia"),
            User(name = "claire", surname = "wilson"),
            User(name = "henry", surname = "clark"),
            User(name = "brooklyn", surname = "hughes"),
            User(name = "leo", surname = "thomas"),
            User(name = "grace", surname = "rodriguez"),
            User(name = "jacob", surname = "lee"),
            User(name = "violet", surname = "lopez"),
            User(name = "luke", surname = "butler"),
            User(name = "zoey", surname = "perez"),
            User(name = "christian", surname = "mitchell"),
            User(name = "mia", surname = "collins"),
            User(name = "wyatt", surname = "young"),
            User(name = "avery", surname = "baker"),
            User(name = "hazel", surname = "lopez"),
            User(name = "liam", surname = "hernandez"),
            User(name = "madison", surname = "hall"),
            User(name = "emma", surname = "king"),
            User(name = "nathan", surname = "martinez"),
            User(name = "lily", surname = "scott")
        )
    }
}