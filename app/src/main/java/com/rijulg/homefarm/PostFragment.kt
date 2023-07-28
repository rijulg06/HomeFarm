package com.rijulg.homefarm

import android.app.Activity
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rijulg.homefarm.databinding.FragmentPostBinding
import com.rijulg.homefarm.models.Post
import com.rijulg.homefarm.models.User

class PostFragment : Fragment() {

    // View binding variables
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private var signedInUser: User? = null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var storageReference: StorageReference

    private var imgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storageReference = FirebaseStorage.getInstance().reference

        firestoreDb = FirebaseFirestore.getInstance()
        firestoreDb.collection("users")
            .document(Firebase.auth.currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
            }
            .addOnFailureListener { userSnapshot ->
                Log.e("PostFragment", "error: $userSnapshot")
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // View binding variables initialized
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Checks if email or password field is empty
        binding.descriptionInput.setOnFocusChangeListener { _, _ ->
            val description = binding.descriptionInput.text.toString()
            if (binding.descriptionInput.hasFocus()) {
                binding.descriptionInputText.error = null
            } else if (TextUtils.isEmpty(description)) {
                binding.descriptionInputText.error = "Email cannot be empty"
            }
        }
        binding.fruitInput.setOnFocusChangeListener { _, _ ->
            val fruit = binding.fruitInput.text.toString()
            if (binding.fruitInput.hasFocus()) {
                binding.fruitInputText.error = null
            } else if (TextUtils.isEmpty(fruit)) {
                binding.fruitInputText.error = "Password cannot be empty"
            }
        }

        val changeImage =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data = it.data
                    imgUri = data?.data
                    binding.imagePreview.isVisible = true
                    binding.imagePreview.setImageURI(imgUri)
                } else {
                    Toast.makeText(requireActivity(), "No image selected", Toast.LENGTH_SHORT).show()
                }
            }

        binding.pickImage.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

        binding.submit.setOnClickListener {

            if (binding.imagePreview.drawable == null) {
                Toast.makeText(requireActivity(), "No image provided", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val description = binding.descriptionInput.text.toString()
            val fruit = binding.fruitInput.text.toString()

            // Checks if email or password is empty
            if (TextUtils.isEmpty(description) || TextUtils.isEmpty(fruit)){
                if (TextUtils.isEmpty(description)) {
                    binding.descriptionInputText.error = "Email cannot be empty"
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(fruit)) {
                    binding.fruitInputText.error = "Password cannot be empty"
                    return@setOnClickListener
                }
                binding.imagePreview.requestFocus()
            }

            binding.submit.isEnabled = false
            val photoUploadUri = imgUri as Uri
            val photoReference = storageReference.child("images/${System.currentTimeMillis()}-photo.jpg")
            photoReference.putFile(photoUploadUri)
                .continueWithTask {
                    // TODO: Add progress check
                    photoReference.downloadUrl
                }.continueWithTask { downloadUrlTask ->
                    val post = Post(
                        binding.descriptionInput.text.toString(),
                        downloadUrlTask.result.toString(),
                        System.currentTimeMillis(),
                        signedInUser,
                        binding.fruitInput.text.toString()
                    )
                    firestoreDb.collection("posts").add(post)
                }.addOnCompleteListener { postCreationTask ->
                    binding.submit.isEnabled = true
                    if (!postCreationTask.isSuccessful) {
                        Log.e("PostFragment", "Exception during Firebase operations: ", postCreationTask.exception)
                        Toast.makeText(requireActivity(), "Failed to save post", Toast.LENGTH_SHORT).show()
                    }
                    binding.imagePreview.setImageResource(0)
                    binding.descriptionInput.text?.clear()
                    binding.fruitInput.text?.clear()
                    val intent = Intent(activity, AppActivity::class.java)
                    startActivity(intent)
                }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}