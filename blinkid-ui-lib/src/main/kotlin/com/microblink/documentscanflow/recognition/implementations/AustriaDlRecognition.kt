package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.austria.AustriaDlFrontRecognizer

class AustriaDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { AustriaDlFrontRecognizer() }

    override fun getSingleSideRecognizers() = listOf(frontRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        result.apply {
            add(R.string.keyLastName, name)
            add(R.string.keyFirstName, firstName)
            add(R.string.keyDateOfBirth, dateOfBirth)
            add(R.string.keyPlaceOfBirth, placeOfBirth)
            add(R.string.keyIssueDate, dateOfIssue)
            addDateOfExpiry(dateOfExpiry.date)
            add(R.string.keyIssuingAuthority, issuingAuthority)
            add(R.string.keyLicenceNumber, licenceNumber)
            add(R.string.keyVehicleCategories, vehicleCategories)
        }

        return FormattingUtils.formatResultTitle(result.firstName, result.name)
    }
}