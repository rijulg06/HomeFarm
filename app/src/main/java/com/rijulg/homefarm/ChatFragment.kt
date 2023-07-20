package com.rijulg.homefarm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.databinding.FragmentChatBinding
import com.rijulg.homefarm.models.Message
import com.rijulg.homefarm.models.Room
import com.rijulg.homefarm.models.User

class ChatFragment : Fragment() {

    // View binding variables
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    // Firestore variable
    private lateinit var firestoreDb: FirebaseFirestore

    // Firebase auth variable
    private lateinit var auth: FirebaseAuth

    private lateinit var roomId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firestore variable initialized
        firestoreDb = FirebaseFirestore.getInstance()

        //Firebase auth variable initialized
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // View binding variables initialized
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = this.arguments
        val toUser = args?.getSerializable("toUser") as User
        val uid1 = auth.currentUser?.uid as String
        val uid2 = toUser.uid
        val roomData = Room(uid1, uid2)

        val room = firestoreDb.collection("rooms").where(Filter.or(
            Filter.and(
                Filter.equalTo("fromUid", uid1),
                Filter.equalTo("toUid", uid2)
            ),
            Filter.and(
                Filter.equalTo("fromUid", uid2),
                Filter.equalTo("toUid", uid1)
            )
        ))

        room.get()
            .addOnSuccessListener { documents ->
                if(documents.isEmpty) {
                    roomId = firestoreDb.collection("rooms").document().id
                    firestoreDb.collection("rooms").document(roomId).set(roomData)
                } else {
                    for(document in documents) {
                        roomId = document.id
                    }
                }
            }

        binding.sendButton.setOnClickListener {

            val messageText = binding.chatText.text.toString()
            binding.chatText.text.clear()
            val message = Message(messageText = messageText, fromUid = uid1)
            firestoreDb.collection("rooms").document(roomId).collection("messages").document().set(message)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}