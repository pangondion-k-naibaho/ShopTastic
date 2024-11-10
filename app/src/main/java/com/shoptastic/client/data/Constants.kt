package com.shoptastic.client.data

class Constants {

    interface URL_CONSTANTS{
        companion object{
            const val API_URL = "https://fakestoreapi.com/"
        }
    }

    interface PREFERENCES{
        companion object{
            const val APP_PREFERENCES = "APP_PREFERENCES"
            const val TOKEN_KEY = "TOKEN_KEY"
            const val USERNAME_KEY = "USERNAME_KEY"
        }
    }

    interface PRODUCT_CATEGORY{
        companion object{
            const val ELECTRONICS = "electronics"
            const val JEWELRY = "jewelery"
            const val MENS_CLOTHING = "men's clothing"
            const val WOMENS_CLOTHING = "women's clothing"
        }
    }

}