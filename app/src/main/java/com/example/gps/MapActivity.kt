//package com.example.gps
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import android.os.Bundle
//import android.os.Looper
//import android.util.Log
//import android.view.KeyEvent
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.gps.databinding.ActivityMapBinding
//import com.example.gps.map.*
//import com.example.gps.map.search.Poi
//import com.example.gps.map.search.Pois
//import com.example.gps.map.search.SearchPoiInfo
//import com.example.gps.map.utility.RetrofitUtil
//import com.google.android.gms.location.*
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.*
//import kotlinx.android.synthetic.main.activity_map.*
//import kotlinx.coroutines.*
//import kotlin.coroutines.CoroutineContext
//
//
//class MapActivity : BaseActivity(), OnMapReadyCallback, CoroutineScope {
//
//
//    private lateinit var job: Job
//
//    override val coroutineContext: CoroutineContext
//        get() = Dispatchers.Main + job
//
//    private lateinit var binding: ActivityMapBinding
//    lateinit var adapter: SearchRecyclerAdapter
//
//    //현재위치
//    private lateinit var map: GoogleMap
//    private var currentSelectMarker: Marker? = null
//    private lateinit var searchResult: SearchResultEntity
//
//    //현재 정보 업데이트 변수
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    private lateinit var locationManager: LocationManager // 안드로이드 에서 위치정보 불러올 때 관리해주는 유틸 클래스
//
//    private lateinit var locationCallback: LocationCallback  // 위치값 요청에 대한 갱신 정보를 받기 위해
//
//    private lateinit var myLocationListener: MyLocationListener // 나의 위치를 불러올 리스너
//
//    companion object {
//        const val SEARCH_RESULT_EXTRA_KEY: String = "SEARCH_RESULT_EXTRA_KEY"
//        const val CAMERA_ZOOM_LEVEL = 17f
//        const val PERMISSION_REQUEST_CODE = 2021
//    }
//
//    val roadList = ArrayList<RoadVO>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMapBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_map)
//        setContentView(binding.root)
//
//        job = Job()
//
//        initAdapter()
//        initViews()
//        bindViews()
//        initData()
//
//        if (::searchResult.isInitialized.not()) {
//            intent?.let {
//                searchResult = it.getParcelableExtra<SearchResultEntity>(SEARCH_RESULT_EXTRA_KEY)
//                    ?: throw Exception("데이터가 존재하지 않습니다.")
//                setupGoogleMap()
//            }
//        }
//
////        bindViews()
//
//        //권한 관련
////        val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
//
////        requirePermissions(permission, 999)
//
//        val etMapSearch = findViewById<EditText>(R.id.etMapSearch)
//        val btnMapSearch = findViewById<Button>(R.id.btnMapSearch)
//        val rvMap = findViewById<RecyclerView>(R.id.rvMap)
//
//        //Template => list_layout.xml
//
//
//        val mapList = ArrayList<MapVO>()
////        mapList.add(MapVO("광주 탑텐", "광주광역시 동구 충장로 87", "광주광역시 동구 충장로2가 8-3"))
////        mapList.add(MapVO("광주 탑텐", "광주광역시 서구 죽봉대로 61", "광주광역시 서구 화정동 12-13"))
////        mapList.add(MapVO("광주 탑텐", "광주광역시 북구 연양로 2-1", "광주광역시 북구 연제동 496-2"))
////        mapList.add(MapVO("광주 탑텐", "광주광역시 동구 중앙로 163", "광주광역시 동구 충장로4가 29-2"))
////        mapList.add(MapVO("광주 탑텐", "광주광역시 남구 서문대로 746", "광주광역시 남구 주월동 1244-7"))
////        mapList.add(MapVO("광주 탑텐", "광주광역시 광산구 장신로 98 2층", "광주광역시 광산구 장덕동 1678 2층"))
//
////        val adapter = MapAdapter(this@MapActivity, mapList)
//
//        //검색한 장소 위치
////        roadList.add(RoadVO(35.148304634523534, 126.91685703775207))
////        roadList.add(RoadVO(35.15881942467587, 126.88206477422064))
////        roadList.add(RoadVO(35.205647328179104, 126.86705354437646 ))
////        roadList.add(RoadVO(35.148701031870175, 126.91335311862461))
////        roadList.add(RoadVO(35.125932310866716, 126.89845372996768))
////        roadList.add(RoadVO(35.19030067229035, 126.82073285851655))
//
//        var road: String
//
////        adapter.setOnItemClickListener(object : MapAdapter.OnItemClickListener{
////            override fun onItemClick(View: View, position: Int) {
////                mMap.clear()
////
////                road = mapList[position].road
////
////                val search = LatLng(roadList[position].latitude, roadList[position].longitude)
////                val markerOptions = MarkerOptions()
////                //MarkerOptions: 마커가 표시될 위치(position), 마커에 표시될 타이틀(title), 마커 클릭시 보여주는 간단한 설명(snippet)
////                markerOptions.position(search)
////                markerOptions.title("광주 탑텐")
////                markerOptions.snippet("옷 가게")
////                //addMarker 메소드로 GoogleMap 객체에 추가해주면 지도에 표시
////                mMap.addMarker(markerOptions)
////
////                //moveCamera 메소드를 사용하여 카메라를 지정한 경도, 위도로 이동
////                //1 단계로 지정하면 세계지도 수준으로 보이고 숫자가 커질수록 상세지도
////                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(search, 15.5f))
////
////            }
////
////        })
//
//
////        val mapFragment = supportFragmentManager
////            .findFragmentById(R.id.map) as SupportMapFragment
////        val mLayout = findViewById<ConstraintLayout>(R.id.layout_main)
//
//        //마지막 위치 기억
////        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//
//        //getMapAsync() 메소드를 호출하여 GoogleMap 객체가 준비될 때 실행될 콜백을 등록
////        mapFragment.getMapAsync(this)
//
//
//
//    }//onCreate 닫힘
//
////    private fun bindViews() = with(binding) {
////        // 현재 위치 버튼 리스너
////        currentLocationButton.setOnClickListener {
////            binding.progressCircular.isVisible = true
////            getMyLocation()
////        }
////    }
//
//    //초기에 보이는 화면 설정
//    override fun onMapReady(map: GoogleMap) {
//        this.map = map
//        currentSelectMarker = setupMarker(searchResult)
//
//        currentSelectMarker?.showInfoWindow()
//    }
//
//
//    private fun setupGoogleMap() {
//        val mapFragment =
//            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this) // callback 구현 (onMapReady)
//
//        // 마커 데이터 보여주기
//
//    }
//    private fun initAdapter() {
//        adapter = SearchRecyclerAdapter()
//    }
//
//    private fun bindViews() = with(binding) {
//        btnMapSearch.setOnClickListener {
//            searchKeyword(etMapSearch.text.toString())
//
//        }
//
//        etMapSearch.setOnKeyListener { v, keyCode, event ->
//            when (keyCode) {
//                KeyEvent.KEYCODE_ENTER -> {
//                    searchKeyword(etMapSearch.text.toString())
//
//
//                    return@setOnKeyListener true
//                }
//            }
//            return@setOnKeyListener false
//        }
//    }
//
//    private fun initViews() = with(binding) {
//        rvMap.adapter = adapter
//
//        // 무한 스크롤 기능 구현
//        rvMap.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                recyclerView.adapter ?: return
//
//                val lastVisibleItemPosition =
//                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//                val totalItemCount = recyclerView.adapter!!.itemCount - 1
//
//                // 페이지 끝에 도달한 경우
//                if (!recyclerView.canScrollVertically(1) && lastVisibleItemPosition == totalItemCount) {
//                    loadNext()
//                }
//            }
//        })
//    }
//
//    private fun loadNext() {
//        if (binding.rvMap.adapter?.itemCount == 0)
//            return
//
//        searchWithPage(adapter.currentSearchString, adapter.currentPage + 1)
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun initData() {
//        adapter.notifyDataSetChanged()
//    }
//
//    private fun setData(searchInfo: SearchPoiInfo, keywordString: String) {
//
//        val pois: Pois = searchInfo.pois
//        // mocking data
//        val dataList = pois.poi.map {
//            SearchResultEntity(
//                name = it.name ?: "빌딩명 없음",
//                fullAddress = makeMainAddress(it),
//                locationLatLng = LocationLatLngEntity(
//                    it.noorLat,
//                    it.noorLon
//                )
//            )
//        }
//        adapter.setSearchResultList(dataList) {
//            Toast.makeText(
//                this,
//                "빌딩이름 : ${it.name}, 주소 : ${it.fullAddress} 위도/경도 : ${it.locationLatLng}",
//                Toast.LENGTH_SHORT
//            )
//                .show()
//
//            // map 액티비티 시작
//            startActivity(Intent(this, MapActivity::class.java).apply {
//                putExtra(SEARCH_RESULT_EXTRA_KEY, it)
//            })
//        }
//        adapter.currentPage = searchInfo.page.toInt()
//        adapter.currentSearchString = keywordString
//    }
//
//    private fun searchKeyword(keywordString: String) {
//        searchWithPage(keywordString, 1)
//    }
//
//    private fun searchWithPage(keywordString: String, page: Int) {
//        // 비동기 처리
//        launch(coroutineContext) {
//            try {
//                if (page == 1) {
//                    adapter.clearList()
//                }
//                // IO 스레드 사용
//                withContext(Dispatchers.IO) {
//                    val response = RetrofitUtil.apiService.getSearchLocation(
//                        keyword = keywordString,
//                        page = page
//                    )
//                    if (response.isSuccessful) {
//                        val body = response.body()
//                        // Main (UI) 스레드 사용
//                        withContext(Dispatchers.Main) {
//                            Log.e("response LSS", body.toString())
//                            body?.let { searchResponse ->
//                                setData(searchResponse.searchPoiInfo, keywordString)
//                            }
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                // error 해결 방법
//                // Permission denied (missing INTERNET permission?) 인터넷 권한 필요
//                // 또는 앱 삭제 후 재설치
//            } finally {
//            }
//        }
//    }
//
//    private fun makeMainAddress(poi: Poi): String =
//        if (poi.secondNo?.trim().isNullOrEmpty()) {
//            (poi.upperAddrName?.trim() ?: "") + " " +
//                    (poi.middleAddrName?.trim() ?: "") + " " +
//                    (poi.lowerAddrName?.trim() ?: "") + " " +
//                    (poi.detailAddrName?.trim() ?: "") + " " +
//                    poi.firstNo?.trim()
//        } else {
//            (poi.upperAddrName?.trim() ?: "") + " " +
//                    (poi.middleAddrName?.trim() ?: "") + " " +
//                    (poi.lowerAddrName?.trim() ?: "") + " " +
//                    (poi.detailAddrName?.trim() ?: "") + " " +
//                    (poi.firstNo?.trim() ?: "") + " " +
//                    poi.secondNo?.trim()
//        }
//
//
//    private fun setupMarker(searchResult: SearchResultEntity): Marker {
//
//        // 구글맵 전용 위도/경도 객체
//        val positionLatLng = LatLng(
//            searchResult.locationLatLng.latitude.toDouble(),
//            searchResult.locationLatLng.longitude.toDouble()
//        )
//
//        // 구글맵 마커 객체 설정
//        val markerOptions = MarkerOptions().apply {
//            position(positionLatLng)
//            title(searchResult.name)
//            snippet(searchResult.fullAddress)
//        }
//
//        // 카메라 줌 설정
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, CAMERA_ZOOM_LEVEL))
//
//        return map.addMarker(markerOptions)!!
//    }
//
//    private fun getMyLocation() {
//        // 위치 매니저 초기화
//        if (::locationManager.isInitialized.not()) {
//            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        }
//
//        // GPS 이용 가능한지
//        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//
//        // 권한 얻기
//        if (isGpsEnable) {
//            when {
//                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && shouldShowRequestPermissionRationale(
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) -> {
//                    showPermissionContextPop()
//                }
//
//                ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED -> {
//                    makeRequestAsync()
//                }
//
//                else -> {
//                    setMyLocationListener()
//                }
//            }
//        }
//    }
//
//    private fun showPermissionContextPop() {
//        AlertDialog.Builder(this)
//            .setTitle("권한이 필요합니다.")
//            .setMessage("내 위치를 불러오기위해 권한이 필요합니다.")
//            .setPositiveButton("동의") { _, _ ->
//                makeRequestAsync()
//            }
//            .create()
//            .show()
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun setMyLocationListener() {
//        val minTime = 3000L // 현재 위치를 불러오는데 기다릴 최소 시간
//        val minDistance = 100f // 최소 거리 허용
//
//        // 로케이션 리스너 초기화
//        if (::myLocationListener.isInitialized.not()) {
//            myLocationListener = MyLocationListener()
//        }
//
//        // 현재 위치 업데이트 요청
//        with(locationManager) {
//            requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                minTime,
//                minDistance,
//                myLocationListener
//            )
//            requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER,
//                minTime,
//                minDistance,
//                myLocationListener
//            )
//        }
//    }
//
//    private fun onCurrentLocationChanged(locationLatLngEntity: LocationLatLngEntity) {
//        map.moveCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    locationLatLngEntity.latitude.toDouble(),
//                    locationLatLngEntity.longitude.toDouble()
//                ), CAMERA_ZOOM_LEVEL
//            )
//        )
//
//        loadReverseGeoInformation(locationLatLngEntity)
//        removeLocationListener() // 위치 불러온 경우 더이상 리스너가 필요 없으므로 제거
//    }
//
//    private fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) {
//        // 코루틴 사용
//        launch(coroutineContext) {
//            try {
//
//                // IO 스레드에서 위치 정보를 받아옴
//                withContext(Dispatchers.IO) {
//                    val response = RetrofitUtil.apiService.getReverseGeoCode(
//                        lat = locationLatLngEntity.latitude.toDouble(),
//                        lon = locationLatLngEntity.longitude.toDouble()
//                    )
//                    if (response.isSuccessful) {
//                        val body = response.body()
//
//                        // 응답 성공한 경우 UI 스레드에서 처리
//                        withContext(Dispatchers.Main) {
//                            Log.e("list", body.toString())
//                            body?.let {
//                                currentSelectMarker = setupMarker(
//                                    SearchResultEntity(
//                                        fullAddress = it.addressInfo.fullAddress ?: "주소 정보 없음",
//                                        name = "내 위치",
//                                        locationLatLng = locationLatLngEntity
//                                    )
//                                )
//                                // 마커 보여주기
//                                currentSelectMarker?.showInfoWindow()
//                            }
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(this@MapActivity, "검색하는 과정에서 에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
//            } finally {
//            }
//        }
//    }
//
//    private fun removeLocationListener() {
//        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
//            locationManager.removeUpdates(myLocationListener) // myLocationListener 를 업데이트 대상에서 지워줌
//        }
//    }
//
//    override fun permissionGranted(requestCode: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override fun permissionDenied(requestCode: Int) {
//        TODO("Not yet implemented")
//    }
//
//    // 권한 요청 결과 처리
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            PERMISSION_REQUEST_CODE -> {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    setMyLocationListener()
//                } else {
//                    Toast.makeText(this, "권한을 받지 못했습니다.", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private fun makeRequestAsync() {
//        // 퍼미션 요청 작업. 아래 작업은 비동기로 이루어짐
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ),
//            PERMISSION_REQUEST_CODE
//        )
//    }
//
//    inner class MyLocationListener : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            // 현재 위치 콜백
//            val locationLatLngEntity = LocationLatLngEntity(
//                location.latitude.toFloat(),
//                location.longitude.toFloat()
//            )
//
//            onCurrentLocationChanged(locationLatLngEntity)
//        }
//
//    }
//
//
//
//
//
//}