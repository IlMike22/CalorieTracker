package com.mind.market.core.domain.use_case

class FilterOutDigits {
    operator fun invoke(text:String):String {
        return text.filter { singleChar ->
            singleChar.isDigit()
        }
    }
}