package com.example.gps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gps.map.MapAdapter
import com.example.gps.map.MapVO
import com.example.gps.map.RoadVO
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


internal class MapActivity : BaseActivity(), OnMapReadyCallback {


    //현재위치
    private lateinit var mMap: GoogleMap

    //현재 정보 업데이트 변수
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var currentMarker: Marker

    private lateinit var discriptor: BitmapDescriptor

    private lateinit var locationCallback: LocationCallback  // 위치값 요청에 대한 갱신 정보를 받기 위해

    val roadList = ArrayList<RoadVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

        requirePermissions(permission, 999)

        val etMapSearch = findViewById<EditText>(R.id.etMapSearch)
        val btnMapSearch = findViewById<Button>(R.id.btnMapSearch)
        val rvMap = findViewById<RecyclerView>(R.id.rvMap)

        //Template => list_layout.xml


        val mapList = ArrayList<MapVO>()
        mapList.add(MapVO("광주 탑텐", "광주광역시 동구 충장로 87", "광주광역시 동구 충장로2가 8-3"))
        mapList.add(MapVO("광주 탑텐", "광주광역시 서구 죽봉대로 61", "광주광역시 서구 화정동 12-13"))
        mapList.add(MapVO("광주 탑텐", "광주광역시 북구 연양로 2-1", "광주광역시 북구 연제동 496-2"))
        mapList.add(MapVO("광주 탑텐", "광주광역시 동구 중앙로 163", "광주광역시 동구 충장로4가 29-2"))
        mapList.add(MapVO("광주 탑텐", "광주광역시 남구 서문대로 746", "광주광역시 남구 주월동 1244-7"))
        mapList.add(MapVO("광주 탑텐", "광주광역시 광산구 장신로 98 2층", "광주광역시 광산구 장덕동 1678 2층"))

        val adapter = MapAdapter(this@MapActivity, mapList)

        btnMapSearch.setOnClickListener {
            rvMap.adapter = adapter
            rvMap.layoutManager = LinearLayoutManager(this)
        }


        //검색한 장소 위치
        roadList.add(RoadVO(35.148304634523534, 126.91685703775207))
        roadList.add(RoadVO(35.15881942467587, 126.88206477422064))
        roadList.add(RoadVO(35.205647328179104, 126.86705354437646 ))
        roadList.add(RoadVO(35.148701031870175, 126.91335311862461))
        roadList.add(RoadVO(35.125932310866716, 126.89845372996768))
        roadList.add(RoadVO(35.19030067229035, 126.82073285851655))

        var road: String

        adapter.setOnItemClickListener(object : MapAdapter.OnItemClickListener{
            override fun onItemClick(View: View, position: Int) {
                mMap.clear()

                road = mapList[position].road

                val search = LatLng(roadList[position].latitude, roadList[position].longitude)
                val markerOptions = MarkerOptions()
                //MarkerOptions: 마커가 표시될 위치(position), 마커에 표시될 타이틀(title), 마커 클릭시 보여주는 간단한 설명(snippet)
                markerOptions.position(search)
                markerOptions.title("광주 탑텐")
                markerOptions.snippet("옷 가게")
                //addMarker 메소드로 GoogleMap 객체에 추가해주면 지도에 표시
                mMap.addMarker(markerOptions)

                //moveCamera 메소드를 사용하여 카메라를 지정한 경도, 위도로 이동
                //1 단계로 지정하면 세계지도 수준으로 보이고 숫자가 커질수록 상세지도
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(search, 15.5f))

            }

        })

//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        val mLayout = findViewById<ConstraintLayout>(R.id.layout_main)

        //마지막 위치 기억
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //getMapAsync() 메소드를 호출하여 GoogleMap 객체가 준비될 때 실행될 콜백을 등록
//        mapFragment.getMapAsync(this)



    }//onCreate 닫힘

    override fun permissionGranted(requestCode: Int) {
        startProcess()
    }

    override fun permissionDenied(requestCode: Int) {
        Toast.makeText(this
            , "권한 승인이 필요합니다."
            , Toast.LENGTH_LONG)
            .show()
    }

    fun startProcess() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)  // 구글 지도를 그려달라는 요청
    }

    //초기에 보이는 화면 설정
    override fun onMapReady(googleMap: GoogleMap) {// onMapReadyCallback 메서드 오버라이드
        mMap = googleMap

        val SEOUL = LatLng(37.56, 126.97)
        val markerOptions = MarkerOptions()
        //MarkerOptions: 마커가 표시될 위치(position), 마커에 표시될 타이틀(title), 마커 클릭시 보여주는 간단한 설명(snippet)
        markerOptions.position(SEOUL)
        markerOptions.title("서울")
        markerOptions.snippet("한국의 수도")
        //addMarker 메소드로 GoogleMap 객체에 추가해주면 지도에 표시
        mMap.addMarker(markerOptions)

        //moveCamera 메소드를 사용하여 카메라를 지정한 경도, 위도로 이동
        //1 단계로 지정하면 세계지도 수준으로 보이고 숫자가 커질수록 상세지도
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15.5f))

        // 현재 위치를 검색하기 위해서 FusedLocationProviderClient 사용
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        updateLocation()

    }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        /* 위치 정보를 요청할 locationRequest 생성하고 정확도와 주기를 설정 */
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
        }

        locationCallback = object : LocationCallback() {  // 해당 주기마다 반환받을 locationCallback
            override fun onLocationResult(p0: LocationResult) {
                p0?.let {
                    for(location in it.locations) {
                        Log.d("Location", "${location.latitude} , ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

    }


    fun setLastLocation(lastLocation: Location) {
        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)  // 전달받은 위치를 좌표로 마커를 생성

        /* 마커 추가 및 옵션 */
        val markerOptions = MarkerOptions()  // 마커 추가
            .position(LATLNG)  // 마커의 좌표
            .title("Here!")  // 마커의 제목
            .snippet("현재 내 위치")

        /* 카메라 위치를 현재 위치로 세팅하고 마커와 함께 지도에 반영 */
        val cameraPosition = CameraPosition.Builder()
            .target(LATLNG)  // 카메라의 목표 지점
            //1 단계로 지정하면 세계지도 수준으로 보이고 숫자가 커질수록 상세지도
            .zoom(15.5f)  // 카메라 줌
            .build()
        // bearing : 지도의 수직선이 북쪽을 기준으로 시계 방향 단위로 측정되는 방향
        // tilt : 카메라의 기울기

        mMap.clear()  // 마커를 지도에 반영하기 전에 clear를 사용해서 이전에 그려진 마커가 있으면 지운다.
        mMap.addMarker(markerOptions)  // 지도에 마커를 추가
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//        moveCamera(mMap)
        // CameraUpdateFactory.newCameraPosition(cameraPosition) : 카메라 포지션에 지도에서 사용할 수 있는 카메라 정보가 생성된다.
        // mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)) : 카메라 포지션을 기준으로 지도의 위치, 배율, 기울기 등이 변경돼서 표시된다.
    }




}