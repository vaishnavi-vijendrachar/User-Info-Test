package com.vaishnavi.optustest.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vaishnavi.optustest.model.Album
import com.vaishnavi.optustest.model.User
import com.vaishnavi.optustest.repository.remote.RetrofitClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository  {

    companion object {
        fun getUserDataFromNetwork(): LiveData<List<User>> {
            var userList = ArrayList<User>()
            var list = MutableLiveData<List<User>>()

            GlobalScope.launch {
                lateinit var user: User
                val call = RetrofitClient.getRetrofitInstance().getUserDetails()
                call.enqueue(object : Callback<List<User>> {
                    override fun onResponse(
                        call: Call<List<User>>,
                        response: Response<List<User>>
                    ) {
                        for (users: User in response.body()!!) {
                            user = User(
                                users.name,
                                users.phone,
                                users.email,
                                users.id
                            )
                            userList.add(user)
                            list.value = userList
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
            return list
        }

            fun getAlbumFromNetwork(otherId : Int) : LiveData<List<Album>>  {
                var albumList = ArrayList<Album>()
                var list  = MutableLiveData<List<Album>>()
                GlobalScope.launch  {
                    val call = RetrofitClient.getRetrofitInstance().getPhotoAlbum()
                    call.enqueue(object : Callback<List<Album>> {
                        override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                            for (albums: Album in response.body()!!) {
                                if(albums.albumId == otherId) {
                                    var album = Album(
                                        albums.albumId,
                                        albums.id,
                                        albums.title,
                                        albums.thumbnailUrl,
                                        albums.url
                                    )
                                    albumList.add(album)
                                }

                                    list.value = albumList
                            }
                        }
                        override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
                 return list
            }
    }
}