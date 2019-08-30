package com.wmapp.common

/**
 * Constant class for common functionality.
 */
class AppConstants {
    companion object {
        const val AUTHORIZATION = "Bearer df7c313b47b7ef87c64c0f5f5cebd6086bbb0fa"
        const val TIME_OUT : Long = 120
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 0x3
        const val REQUEST_CHECK_SETTINGS = 0x1
        const val CARID = "CARID"
        const val BOOK_URL = "https://4i96gtjfia.execute-api.eu-central-1.amazonaws.com/default/wunderfleet-recruiting-mobile-dev-quick-rental"
        var LOCATION_PERMISSION_DENIED = false
        const val CAR_FEED_REQ = 1
        const val CAR_DETAILS_REQ = 2
        const val CAR_BOOK_REQ = 3
    }
}