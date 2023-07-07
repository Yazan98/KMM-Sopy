# Sopy

A Kotlin Multiplatform Mobile library to Share Utility Classes between Android, IOS Apps to Lead Mobile Apps by Domain Layer and UseCases

## Library Description

This Library Share Common Parts between Android and IOS Applications and the Idea of this Library is to Control the Applications from UseCases and Domain Layer
No Logic on UI or ViewModel Level, the Whole Logic Should be Inside UseCases and for Api Part Sopy Use Ktor Client as a Http Client

## Library Objective

The Main Idea of this Library is to Build common Implementation of the Domain Layer between Android, IOS Apps
and Share Same Logic by UseCases, in Sopy the UseCases are the Start Point in each Feature

## Supported Platforms
1. Android
2. IOS (Darwin, Legacy Darwin)

## Library Components
1. Local Storage (Shared Prefs, UserDefaults)
2. UseCases and Constraints
3. Api Requests (OneRequest, Crud Operations)
4. Ktor Client Configurations (Android, IOS, IOS Darwin Legacy)

## Library Versions
1. Ktor Version: 2.3.0
2. Java Version: 17
3. Kotlin Coroutines: 1.6.4
4. Android Lifecycle ViewModels: 2.5.1

## IOS Supported Architectures
1. Arm32
2. Arm64
3. x64

## Library Builds Details

> Builds Location in Releases Tab

Library Builds Available to Download on Android, IOS by Build Files

1. Android Builds .aar File (Debug, Release) Available
2. IOS Builds .zip Available for All Supported Architectures (Debug, Release)
3. Android Min SDK Version: 21

## Library Gradle Installation

1. Dependencies Details
```
dependencies {

    // Common Main
    implementation "com.yazantarifi:sopy:1.0.1"
    
    // Android
    implementation "com.yazantarifi:sopy-android:1.0.1"
    
    // IOS
    implementation "com.yazantarifi:sopy-iosx64:1.0.1"
    implementation "com.yazantarifi:sopy-iosarm64:1.0.1"
    implementation "com.yazantarifi:sopy-iosarm32:1.0.1"
}
```

2. KMM Apps Details

```
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.yazantarifi:sopy:1.0.1")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("com.yazantarifi:sopy-android:1.0.1")
            }
        }

        val iosX64Main by getting {
            dependencies {
                implementation("com.yazantarifi:sopy-iosx64:1.0.1")
            }
        }
        
        val iosArm64Main by getting {
            dependencies {
                implementation("com.yazantarifi:sopy-iosarm64:1.0.1")
            }
        }
        
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation("io.ktor:ktor-client-ios:2.2.1")
                implementation("io.ktor:ktor-client-darwin:2.2.1")
            }

            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }

```

## Classes Description
1. SopyOneRequest: Execute Ktor Api Request and Handle Response to UseCase
2. SopyCrudRequest: Execute Ktor Api Request (Crud Operations) and Handle Response to UseCase
3. SopyStorageKeyValue: Local Storage Key Value (SharedPrefs, UserDefaults)
4. SopyUseCase: UseCases Implementation with Ktor Client and State Management (Success, Failed, Loading)
5. SopyDirectUseCase: UseCases Implementation with Ktor Client like SopyUseCase but without Input Type, UseCases to Return Results Only
6. SopyUseCaseAlias: Singleton Instances Helpful in Hilt Scopes in Android Apps Only
7. SopyViewModel: ViewModel Implementation with UseCases and Lifecycle
8. SopyRequestInterceptor: Ktor Request Interceptor to Send Common Headers in All Apis
9. SopyApplicationConfigurations: Application Configurations and General Configs : Registered in App Delegate and Application Scope Only
10. SopyBaseViewModel: Base ViewModels Implementation (IOS Only)


## Ktor IOS Specific Implementation
1. SopyHttpBaseClient: Ktor Darwin Engine : x64, Arm64
2. SopyLegacyHttpBaseClient: Ktor Darwin Legacy: Arm32 Only

## Library Usage

1. Simple UseCase Implementation of a Loading, Failed, Success State and Api Requests

```
class GetHomeScreenItemsUseCase constructor(): SopyUseCase<GetHomeScreenItemsUseCase.RequestValue, List<RadioHomeItem>>() {

    private val featuredListApiClient: SpotifyFeaturedPlaylistsApiRequest by lazy {
        SpotifyFeaturedPlaylistsApiRequest()
    }

    private val newReleasesApiClient: SpotifyNewAlbumsReleasesApiRequest by lazy {
        SpotifyNewAlbumsReleasesApiRequest()
    }

    private val categoriesApiClient: SpotifyGetCategoriesApiRequest by lazy {
        SpotifyGetCategoriesApiRequest(false)
    }

    private val playlistsCategoryApiClient: SpotifyGetCategoryPlaylistApiRequest by lazy {
        SpotifyGetCategoryPlaylistApiRequest()
    }

    override fun isConstraintsSupported(): Boolean {
        return false
    }

    override suspend fun build(requestValue: RequestValue) {
        val screenItems = arrayListOf<RadioHomeItem>()
        onSubmitLoadingState(true)

        screenItems.add(HomeHeaderItem(
            RadioApplicationMessages.getMessage("home_title"),
            RadioApplicationMessages.getMessage("home_des"),
        ))

        screenItems.add(HomeLayoutDesignItem(
            HomeLayoutDesignItem.SCROLL_H,
            RadioApplicationMessages.getMessage("home_change_design"),
        ))

        if (featuredListApiClient.isRequestListenerAttachNeeded()) {
            featuredListApiClient.addHttpClient(getHttpClientInstance())
            featuredListApiClient.addRequestListener(object :
                SopyRequestListener<SpotifyFeaturedPlaylistsResponse> {
                override fun onSuccess(responseValue: SpotifyFeaturedPlaylistsResponse) {
                    val playlists = ArrayList<RadioPlaylist>()
                    responseValue.playlists?.items?.let {
                        playlists.addAll(it.map {  playlist ->
                            var imageUrl: String = ""
                            playlist.images?.forEach {
                                imageUrl = it.url ?: ""
                            }

                            RadioPlaylist(
                                id = playlist.id ?: "",
                                image = imageUrl,
                                name = playlist.name ?: "",
                                ownerName = playlist.owner?.name,
                                numberOfTracks = playlist.tracks?.total ?: 0,
                                RadioApplicationMessages.getMessage("loading_image")
                            )
                        })
                    }

                    screenItems.add(HomePlaylistsItem(
                        RadioApplicationMessages.getMessage("featured_playlists"),
                        RadioApplicationMessages.getMessage("loading_image"),
                        playlists
                    ))
                }

                override fun onError(error: Throwable) {
                    // Will Not Show the Item
                }
            })
        }

        if (newReleasesApiClient.isRequestListenerAttachNeeded()) {
            newReleasesApiClient.addHttpClient(getHttpClientInstance())
            newReleasesApiClient.addRequestListener(object :
                SopyRequestListener<SpotifyAlbumsResponse> {
                override fun onSuccess(responseValue: SpotifyAlbumsResponse) {
                    val albums = ArrayList<RadioAlbum>()
                    responseValue.albums?.items?.let {
                        albums.addAll(it.map { album ->
                            var imageUrl: String = ""
                            album.images?.get(0)?.let {
                                imageUrl = it.url ?: ""
                            }

                            RadioAlbum(
                                id = album.id ?: "",
                                name = album.name ?: "",
                                image = imageUrl ?: "",
                                releaseDate = album.releaseDate ?: "",
                                numberOfTracks = album.numberOfTracks ?: 0,
                                artists = album.artists?.map { it.name ?: "" } ?: arrayListOf(),
                                loadingMessage = RadioApplicationMessages.getMessage("loading_image"),
                            )
                        })
                    }

                    screenItems.add(HomeAlbumsItem(
                        RadioApplicationMessages.getMessage("albums"),
                        RadioApplicationMessages.getMessage("loading_image"),
                        albums
                    ))
                }

                override fun onError(error: Throwable) {
                    // Will Not Show the Item
                }
            })
        }

        if (categoriesApiClient.isRequestListenerAttachNeeded()) {
            categoriesApiClient.addHttpClient(getHttpClientInstance())
            categoriesApiClient.addRequestListener(object :
                SopyRequestListener<SpotifyCategoriesResponse> {
                override fun onSuccess(responseValue: SpotifyCategoriesResponse) {
                    val categories = ArrayList<RadioCategoryItem>()
                    responseValue.categories?.items?.let {
                        categories.addAll(it.map {
                            var imageUrl: String = ""
                            it.icons?.get(0)?.let {
                                imageUrl = it.url ?: ""
                            }

                            RadioCategoryItem(
                                it.id ?: "",
                                it.name ?: "",
                                imageUrl,
                                RadioApplicationMessages.getMessage("loading_image")
                            )
                        })
                    }

                    screenItems.add(HomeCategoriesItem(
                        RadioApplicationMessages.getMessage("categories"),
                        RadioApplicationMessages.getMessage("loading_image"),
                        categories
                    ))
                }

                override fun onError(error: Throwable) {
                    // Will Not Show the Item
                }
            })
        }

        if (playlistsCategoryApiClient.isRequestListenerAttachNeeded()) {
            playlistsCategoryApiClient.addHttpClient(getHttpClientInstance())
            playlistsCategoryApiClient.addRequestListener(object :
                SopyRequestListener<SpotifyFeaturedPlaylistsResponse> {
                override fun onSuccess(responseValue: SpotifyFeaturedPlaylistsResponse) {
                    val playlists = ArrayList<RadioPlaylist>()
                    responseValue.playlists?.items?.let {
                        playlists.addAll(it.map {  playlist ->
                            var imageUrl: String = ""
                            playlist.images?.forEach {
                                imageUrl = it.url ?: ""
                            }

                            RadioPlaylist(
                                id = playlist.id ?: "",
                                image = imageUrl,
                                name = playlist.name ?: "",
                                ownerName = playlist.owner?.name,
                                numberOfTracks = playlist.tracks?.total ?: 0,
                                RadioApplicationMessages.getMessage("loading_image")
                            )
                        })
                    }

                    screenItems.add(HomePlaylistsItem(
                        responseValue.sectionName ?: "",
                        RadioApplicationMessages.getMessage("loading_image"),
                        playlists
                    ))
                }

                override fun onError(error: Throwable) {
                    // Will Not Show the Item
                }
            })
        }

        val headers = SpotifyApiHeadersBuilder.getApplicationBearerTokenHeaders(requestValue.token)
        featuredListApiClient.executeRequest(Unit, headers)
        newReleasesApiClient.executeRequest(Unit, headers)
        categoriesApiClient.executeRequest(Unit, headers)

        if (requestValue.isNotificationPermissionShouldShow) {
            screenItems.add(HomeNotificationPermissionItem(
                true,
                RadioApplicationMessages.getMessage("notification_permission_warning_message"),
                RadioApplicationMessages.getMessage("notification_permission_title"),
                "",
                RadioApplicationMessages.getMessage("notification_permission_message"),
                RadioApplicationMessages.getMessage("notification_permission_enable"),
                RadioApplicationMessages.getMessage("notification_permission_disable"),
                requestValue.isNotificationsEnabled
            ))
        }

        playlistsCategoryApiClient.executeRequest(SpotifyGetCategoryPlaylistApiRequest.RequestParams(
            RadioApplicationMessages.getMessage("top_pop"),
            "0JQ5DAqbMKFEC4WFtoNRpw"
        ), headers)

        playlistsCategoryApiClient.executeRequest(SpotifyGetCategoryPlaylistApiRequest.RequestParams(
            RadioApplicationMessages.getMessage("top_mood"),
            "0JQ5DAqbMKFzHmL4tf05da"
        ), headers)

        playlistsCategoryApiClient.executeRequest(SpotifyGetCategoryPlaylistApiRequest.RequestParams(
            RadioApplicationMessages.getMessage("top_gaming"),
            "0JQ5DAqbMKFCfObibaOZbv"
        ), headers)

        playlistsCategoryApiClient.executeRequest(SpotifyGetCategoryPlaylistApiRequest.RequestParams(
            RadioApplicationMessages.getMessage("top_workout"),
            "0JQ5DAqbMKFAXlCG6QvYQ4"
        ), headers)

        screenItems.add(HomeOpenSpotifyAppItem(
            RadioApplicationMessages.getMessage("spotify_app_open_title"),
            RadioApplicationMessages.getMessage("spotify_app_open_message"),
            RadioApplicationMessages.getMessage("spotify_app_open_button"),
        ))

        onSubmitLoadingState(false)
        onSubmitSuccessState(screenItems)
    }

    data class RequestValue(
        val token: String,
        val isNotificationPermissionShouldShow: Boolean,
        val isNotificationsEnabled: Boolean
    )

    override fun clear() {
        super.clear()
        featuredListApiClient.clear()
        newReleasesApiClient.clear()
        categoriesApiClient.clear()
        playlistsCategoryApiClient.clear()
    }
}
```

2. Api Request Implementation with Ktor Client

```
class SpotifyFeaturedPlaylistsApiRequest: SopyOneRequest<Unit, SpotifyFeaturedPlaylistsResponse>() {
    override suspend fun executeRequest(
        requestBody: Unit,
        headers: List<Pair<String, String>>,
        params: HashMap<String, String>
    ) {
        try {
            val response = httpClient?.get(getRequestUrl()) {
                headers.forEach {
                    header(it.first, it.second)
                }
            }

            if (isSuccessResponse(response?.status ?: HttpStatusCode.BadRequest)) {
                response?.body<SpotifyFeaturedPlaylistsResponse>()?.let {
                    requestListener?.onSuccess(it)
                }
            } else {
                requestListener?.onError(Throwable(response?.bodyAsText()))
            }
        } catch (ex: Exception) {
            requestListener?.onError(ex)
        }
    }

    override fun getRequestUrl(): String {
        return "https://api.spotify.com/v1/browse/featured-playlists"
    }
    
}
```

3. ViewModel Implementation (Hilt Dependency Injection)

```
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeScreenItems: GetHomeScreenItemsUseCase,
    private val categoriesUseCase: GetCategoriesUseCase,
    private val accountInfoUseCase: GetAccountTreeInfoUseCase,
    private val storageProvider: SopyStorageProvider
): SopyViewModel<HomeAction>() {

    var selectedLayoutDesignMode: Int = HomeLayoutDesignItem.SCROLL_H
    val feedLoadingListener by lazy { mutableStateOf(false) }
    var feedContentListener = mutableStateListOf<RadioHomeItem>()

    val categoriesListListener: MutableState<ArrayList<RadioCategoryItem?>> by lazy { mutableStateOf(arrayListOf()) }
    val categoriesLoadingListener by lazy { mutableStateOf(false) }

    val accountLoadingListener by lazy { mutableStateOf(false) }
    val accountInfoListListener: MutableState<ArrayList<RadioAccountItem>> by lazy { mutableStateOf(arrayListOf()) }

    override suspend fun onNewActionTriggered(action: HomeAction) {
        when (action) {
            is HomeAction.GetFeed -> onGetHomeScreenFeedInfo(action.context)
            is HomeAction.GetCategoriesAction -> onGetCategories()
            is HomeAction.GetAccountInfoAction -> onGetAccountInfo()
            is HomeAction.RemoveNotificationPermissionAction -> onRemoveNotificationPermissionItem()
        }
    }

    private fun onGetCategories() {
        if (categoriesListListener.value.isNotEmpty()) return
        categoriesUseCase.execute(
            storageProvider.getAccessToken(),
            object : SopyUseCaseListener {
                override fun onStateUpdated(newState: SopyState) {
                    scope.launch(Dispatchers.Main) {
                        when (newState) {
                            is SopyEmptyState -> {}
                            is SopyLoadingState -> categoriesLoadingListener.value = newState.isLoading
                            is SopyErrorState -> errorMessageListener.value = newState.exception.message ?: ""
                            is SopySuccessState -> (newState.payload as? List<RadioCategoryItem>)?.let {
                                categoriesListListener.value.addAll(it)
                            }
                        }
                    }
                }
            }
        )
    }

    private fun onGetAccountInfo() {
        if (accountInfoListListener.value.isNotEmpty()) return
        accountInfoUseCase.execute(
            storageProvider.getAccessToken(),
            object : SopyUseCaseListener {
                override fun onStateUpdated(newState: SopyState) {
                    scope.launch(Dispatchers.Main) {
                        when (newState) {
                            is SopyEmptyState -> {}
                            is SopyLoadingState -> accountLoadingListener.value = newState.isLoading
                            is SopyErrorState -> errorMessageListener.value = newState.exception.message ?: ""
                            is SopySuccessState -> (newState.payload as? List<RadioAccountItem>)?.let {
                                accountInfoListListener.value.addAll(it)
                            }
                        }
                    }
                }
            }
        )
    }

    private fun onGetHomeScreenFeedInfo(context: Context) {
        if (feedContentListener.isNotEmpty()) return
        getHomeScreenItems.execute(
            GetHomeScreenItemsUseCase.RequestValue(storageProvider.getAccessToken(), android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R, NotificationManagerCompat.from(context).areNotificationsEnabled()),
            object : SopyUseCaseListener {
                override fun onStateUpdated(newState: SopyState) {
                    scope.launch(Dispatchers.Main) {
                        when (newState) {
                            is SopyEmptyState -> {}
                            is SopyLoadingState -> feedLoadingListener.value = newState.isLoading
                            is SopyErrorState -> errorMessageListener.value = newState.exception.message ?: ""
                            is SopySuccessState -> (newState.payload as? List<RadioHomeItem>)?.let {
                                feedContentListener.addAll(it)
                            }
                        }
                    }
                }
            }
        )
    }

    override fun getSupportedUseCases(): ArrayList<SopyUseCaseType> {
        return arrayListOf(getHomeScreenItems, categoriesUseCase, accountInfoUseCase)
    }

}
```

