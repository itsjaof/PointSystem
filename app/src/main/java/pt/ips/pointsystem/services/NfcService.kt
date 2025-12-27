package pt.ips.pointsystem.services

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log

class NfcService(
    private val activity: Activity,
    private val onTagRead: (String) -> Unit,
) : NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter ?= NfcAdapter.getDefaultAdapter(activity)

    private val flags = NfcAdapter.FLAG_READER_NFC_A or
            NfcAdapter.FLAG_READER_NFC_B or
            NfcAdapter.FLAG_READER_NFC_F or
            NfcAdapter.FLAG_READER_NFC_V or
            NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK

    private  val options = Bundle().apply {
        putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
    }

    fun enableReader() {
        Log.d("NfcService", "Leitor ativado")
        nfcAdapter?.enableReaderMode(activity, this, flags, options)
    }

    fun disableReader() {
        Log.d("NfcService", "Leitor desativado")
        nfcAdapter?.disableReaderMode(activity)
    }

    override fun onTagDiscovered(tag: Tag?) {
        Log.d("NfcService", "Tag detetada fisicamente!")

        tag?.let {
            val idBytes = it.id
            val idHex = idBytes.joinToString("") { byte -> "%02x".format(byte) }

            Log.d("NfcService", "ID Convertido: $idHex")

            activity.runOnUiThread { onTagRead(idHex) }
        }
    }
}