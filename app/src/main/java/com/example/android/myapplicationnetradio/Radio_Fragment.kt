package com.example.android.myapplicationnetradio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_radio.*

/**
 * A simple [Fragment] subclass.
 * Use the [Radio_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Radio_Fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_radio, container, false)

        val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val nameObserver = Observer<String> { newName ->
            println("Second Fragment Observer !!!! NewName ="+newName)
            if (newName != "na" ) {
                radioSelectedName.text = viewModel.dataset[newName.toInt()].title+"\n"
                radioSelectedName.append(viewModel.dataset[newName.toInt()].sources[0])
                uPnpRadio(viewModel.dataset[newName.toInt()].title, viewModel.dataset[newName.toInt()].sources[0])
            }
        }
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.selectedRadio.observe(viewLifecycleOwner, nameObserver)

        return rootView
    }

    fun uPnpRadio( Name : String, Url : String) {
        var Soap = "<?xml version=\"1.0\"?>\n"
        Soap = Soap + "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
        Soap = Soap + "<SOAP-ENV:Body><m:SetAVTransportURI xmlns:m=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"ui4\">0</InstanceID>"
        Soap = Soap + "<CurrentURI xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"string\">"
        Soap = Soap + Url
        Soap = Soap + "</CurrentURI>"
        Soap = Soap + "<CurrentURIMetaData xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"string\">&lt;DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"&gt;&lt;item id=\"0$:items$*c17\" parentID=\"0$:items\" restricted=\"1\" microsoft:cpId=\"{2784BECB-B174-4BD2-95A3-7DB9951DCF0B}\" microsoft:trackId=\"1\" xmlns:microsoft=\"urn:schemas-microsoft-com:WMPNSS-1-0/\"&gt;&lt;dc:title&gt;"
        Soap = Soap + Name
        Soap = Soap + "&lt;/dc:title&gt;&lt;res protocolInfo=\"http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_FLAGS=01700000000000000000000000000000\"&gt;"
        Soap = Soap + Url
        Soap = Soap + "&lt;/res&gt;&lt;upnp:class&gt;object.item.audioItem.audioBroadcast&lt;/upnp:class&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;</CurrentURIMetaData>"
        Soap = Soap + "</m:SetAVTransportURI></SOAP-ENV:Body></SOAP-ENV:Envelope>"
        var action = "\"urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI\""
        YamahaSendNetworkPostMsg(Soap, action)
        uPnpPlay()
    }

    fun uPnpPlay() {
        var Soap = "<?xml version=\"1.0\"?>\n"
        Soap = Soap + "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><SOAP-ENV:Body><m:Play xmlns:m=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"ui4\">0</InstanceID><Speed xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"string\">1</Speed></m:Play></SOAP-ENV:Body></SOAP-ENV:Envelope>"
        var action = "\"urn:schemas-upnp-org:service:AVTransport:1#Play\""
        YamahaSendNetworkPostMsg(Soap, action)
    }

    fun YamahaSendNetworkPostMsg(msg: String, action : String) {
        println("Msg =$msg")

        var YamahaIP       = "192.168.1.12"
        var YamahaURI   = "http://$YamahaIP:8080/AVTransport/ctrl"
        var YamahaError     = ""

        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
                Method.POST,
                YamahaURI,
                Response.Listener { response -> System.out.println(response.toString())
                },
                Response.ErrorListener { error -> YamahaError = error.toString()
                    println("Yamana Network error :"+YamahaError )
                }
        )
        {
            override fun getBody(): ByteArray { return msg.toByteArray()  }
            override fun getHeaders(): Map<String, String>? {
                val headers: MutableMap<String, String> =
                        HashMap()
                headers["Content-Type"] = "text/xml;charset=\"utf-8\""
                headers["User-Agent"] = "Microsoft-Windows/10.0 UPnP/1.0 Microsoft-DLNA DLNADOC/1.50"
                headers["FriendlyName.DLNA.ORG"] = "DESKTOP-1B0OHPK"
                headers["SOAPAction"] = action
                return headers
            }
        }
        queue.add(stringRequest)
    }


}
