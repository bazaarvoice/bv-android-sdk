package com.bazaarvoice.bvsdkdemoandroid

import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * For demo purposes only.
 * Encoding the UAS must be done on the server side to preserve the secrecy of the shared secret key.
 */
internal object DemoUserTokenFactory {

    var UAS:String? = null
    private val secureRandom = SecureRandom()
    @JvmStatic
    @JvmName("generateUserToken")
    internal fun generateUserToken(userEmail: String, userId: String, encodingKey: String): String {
        val userToken = StringBuilder("maxage=30").append("&HOSTED=VERIFIED")
        userToken.append("&date=").append(getDate())
        userToken.append("&emailaddress=").append(userEmail)
        userToken.append("&userid=").append(userId)

        val stringBytes = userToken.toString().toByteArray()
        return sha256(stringBytes, encodingKey) + stringBytes.toHex()
    }

    private fun sha256(bytes: ByteArray, encodingKey: String): String {
        val mac = Mac.getInstance("HMACSHA256")
        mac.init(SecretKeySpec(encodingKey.toByteArray(), mac.algorithm))
        return mac.doFinal(bytes).toHex()
    }

    @JvmStatic
    fun ByteArray.toHex(): String {
        val formatter = Formatter()
        for (b: Byte in this) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }

    private fun getDate(): String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        return dateFormat.format(calendar.time)
    }

    fun generateMockUserToken(): String {
        if(UAS != null) {
            return UAS!!
        }
        val mockEncodingKey = generateRandomKey().toHex();
        val id = Date().time
        val mockEmail = "bvtestuser$id@bv.com"
        val mockUserId = "local-$id"
        UAS = generateUserToken(mockEmail, mockUserId, mockEncodingKey)
        return UAS!!
    }

    private fun generateRandomKey(): ByteArray {
        val key = ByteArray(32)
        secureRandom.nextBytes(key)
        return key
    }
}