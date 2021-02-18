package com.vipuldamor87.maps

import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import org.json.JSONException
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var allPoints: MutableList<LatLng> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        var layer: GeoJsonLayer? = null
        var style: GeoJsonPolygonStyle

        if (googleMap != null) {
            mMap = googleMap
        }

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(42.7, -103.1)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in USA"))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                sydney, 12.0f
            )
        )
        allPoints.add(sydney)


        mMap.setOnMapClickListener {
            layer?.removeLayerFromMap()
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                it.latitude,
                it.longitude,
                1
            )

            val address: String = addresses[0]
                .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            val city: String = addresses[0].getLocality()
            val state: String = addresses[0].getAdminArea()
            val country: String = addresses[0].getCountryName()
            Log.d("COUNTRY", country)
            if (country != "United States") {
                Toast.makeText(this, "this is Outside of USA", Toast.LENGTH_SHORT).show()
            } else {
                allPoints.add(it)
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(it).title(state))
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, 12.0f
                    )
                )

                try {
                    layer = GeoJsonLayer(mMap, R.raw.get_geojson, applicationContext)
                    style = layer!!.defaultPolygonStyle
                    style.strokeColor = Color.RED
                    style.strokeWidth = 5f
                    layer!!.addLayerToMap()
                    mMap.setLatLngBoundsForCameraTarget(layer!!.boundingBox)


                } catch (ex: IOException) {
                    Log.e("IOException", ex.getLocalizedMessage())
                } catch (ex: JSONException) {
                    Log.e("JSONException", ex.localizedMessage)
                }


            }
        }
    }
}