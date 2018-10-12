package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.ireland.IrelandDlFrontRecognizer

class IrelandDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { IrelandDlFrontRecognizer() }

    override fun getSingleSideRecognizers() = listOf(frontRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        result.apply {
            add(R.string.keyLastName, surname)
            add(R.string.keyFirstName, firstName)
            add(R.string.keyDateOfBirth, dateOfBirth)
            add(R.string.keyPlaceOfBirth, placeOfBirth)
            add(R.string.keyIssueDate, dateOfIssue)
            add(R.string.keyIssuedBy, issuedBy)
            addDateOfExpiry(dateOfExpiry.date)
            add(R.string.keyDriverNumber, driverNumber)
            add(R.string.keyLicenceNumber, licenceNumber)
            add(R.string.keyAddress, address)
            add(R.string.keyLicenceCategories, licenceCategories)
        }

        return FormattingUtils.formatResultTitle(result.firstName, result.surname)
    }
}