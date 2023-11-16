package com.example.atelier5


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val URL_DATA = "https://jsonplaceholder.typicode.com/users"
    private lateinit var listUser: ArrayList<User>
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisez la liste des utilisateurs si nécessaire
        listUser = ArrayList()

        // Initialiser le RecyclerView et l'adapter
        recyclerView = findViewById(R.id.recyclerViewUsers)
        userAdapter = UserAdapter(listUser)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Appel de la méthode load_user
        load_user()
    }

    private fun load_user() {
        val stringRequest = StringRequest(
            Request.Method.GET, URL_DATA,
            Response.Listener<String> { response ->
                try {
                    val array = JSONArray(response)

                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)

                        val name = obj.getString("name")
                        val username = obj.getString("username")
                        val email = obj.getString("email")

                        val user = User(name, username, email)
                        listUser.add(user)
                    }

                    // Mettez à jour l'adapter lorsque la liste des utilisateurs est modifiée
                    userAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(
                    this@MainActivity,
                    error?.message + " error Loading Users",
                    Toast.LENGTH_LONG
                ).show()
            })

        Volley.newRequestQueue(this).add(stringRequest)
    }}