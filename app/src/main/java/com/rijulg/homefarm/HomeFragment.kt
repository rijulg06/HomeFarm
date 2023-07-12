package com.rijulg.homefarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rijulg.homefarm.databinding.FragmentHomeBinding
import com.rijulg.homefarm.models.Post
import com.rijulg.homefarm.recyclerView.PostsAdapter

class HomeFragment : Fragment() {

    // View binding variables
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Firestore variable
    private lateinit var firestoreDb: FirebaseFirestore

    // Recycler view variables
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firestore variable initialized
        firestoreDb = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // View binding initialized
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Query/read Firestore data
        val postsReference = firestoreDb
            .collection("posts")
            .limit(20)
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)
        postsReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Post::class.java)
            recyclerView = view.findViewById(R.id.recycler_view)!!
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = activity?.let { PostsAdapter(it, postList) }
            (recyclerView.layoutManager as LinearLayoutManager).onRestoreInstanceState(recyclerViewState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}