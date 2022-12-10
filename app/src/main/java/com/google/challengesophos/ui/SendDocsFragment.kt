package com.google.challengesophos.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.challengesophos.R
import com.google.challengesophos.Repository.model.DocItemsPost
import com.google.challengesophos.ViewModel.PostDocViewModel
import com.google.challengesophos.databinding.FragmentSendDocsBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat

import java.util.*

class SendDocsFragment : Fragment(R.layout.fragment_send_docs), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentSendDocsBinding? = null

    private val postDocViewModel: PostDocViewModel by viewModels()




    //Adapter for the spinner of docs type
    lateinit var arrayAdapterTypeDocs: ArrayAdapter<String>

    //Adapter fot the spiner of cities
    lateinit var arrayAdapterCities: ArrayAdapter<String>

    //The code of the permission for the camera
    private val PERMISSION_CAMARA: Int = 100
    private val CAMARA_REQUEST_CODE: Int = 101

    //The code of the permission for the storage
    private val PERMISSION_EXTERNAL_STORAGE: Int = 100
    private val IMAGE_REQUEST_CODE: Int = 102

    //Declaring all the variables needed to send the post
    private lateinit var citySelected: String
    private lateinit var typeDocsSelected: String
    private val calendar: Calendar = Calendar.getInstance()
    private val currentDate = SimpleDateFormat("d/M/yyyy").format(calendar.time)


    //private val currentDate = DateFormat.getDateInstance().format(calendar.time)
    private var imageTakenBase64 = ""


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //binding initialized
        _binding = FragmentSendDocsBinding.inflate(inflater, container, false)


        //enables the menu in the fragment
        setHasOptionsMenu(true)

        //puts the name to the appbar
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.come_back)
        //Sets the back arrow and the icon for it
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_light)


        //Adapter for the spinner of docs type
        arrayAdapterTypeDocs =
            ArrayAdapter<String>(requireContext(), R.layout.spinner_styles_light)

        //Adapter for the spinner of cities
        arrayAdapterCities = ArrayAdapter<String>(requireContext(), R.layout.spinner_styles_light)

        //String for type of ID
        val typeID = getString(R.string.type_ID)

        //puts info inside the spinner
        arrayAdapterTypeDocs.addAll(Arrays.asList(typeID, "CC", "TI", "PA", "CE"))

        //Observes the cities that the Api brings

        postDocViewModel.citiesLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            //Strings used to translate
              val city:String = getString(R.string.city)
            arrayAdapterCities.addAll(
                Arrays.asList(
                    city,
                    postDocViewModel.citiesLiveData.value?.get(0).toString(),
                    postDocViewModel.citiesLiveData.value?.get(1).toString(),
                    postDocViewModel.citiesLiveData.value?.get(2).toString(),
                    postDocViewModel.citiesLiveData.value?.get(3).toString(),
                    postDocViewModel.citiesLiveData.value?.get(4).toString(),
                    postDocViewModel.citiesLiveData.value?.get(5).toString()
                )

            )
        })


        //Establishes the array as the adapter of the spinner
        binding.spDocType.adapter = arrayAdapterTypeDocs
        binding.spDocCity.adapter = arrayAdapterCities

        //Configures the content of the selection
        binding.spDocType.onItemSelectedListener = this
        binding.spDocCity.onItemSelectedListener = this


        //bring the arg of the email
        binding.etEmailSendDocs.setText(arguments?.getString("user_email"))

        binding.ivTakePhotoDocs.setOnClickListener {
            askForCameraPermission()
        }

        binding.btnAttachDoc.setOnClickListener {
            askForFilesPermission()
        }

        //Observes the post method and its arguments
        postDocViewModel.docModel.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            postDocViewModel.postDoc(getInformationForPosting())
        })

        binding.btnSendDoc.setOnClickListener {
            //String variable for the toast
            val documentSent:String = getString(R.string.toast_sent)
            val documentSentFail:String = getString(R.string.toast_sent_fail)

            when (validateUsersDataForPost()) {
                true -> {
                    postDocViewModel.postDoc(getInformationForPosting())
                    showMessage(documentSent)
                    println(getInformationForPosting())
                    view?.findNavController()?.navigate(
                        SendDocsFragmentDirections.actionSendDocsFragmentSelf(
                            arguments?.getString(
                                "user_email"
                            )
                        )
                    )
                }
                else -> showMessage(documentSentFail)
            }
        }

        return binding.root


    }


    //Implemented methods of the interface AdapterView.OnItemSelectedListener (selected item)
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent?.id) {
            //Saves the answer of the user of the docs type selected
            R.id.spDocType -> typeDocsSelected = arrayAdapterTypeDocs.getItem(position).toString()
            //Saves the answer of the city selected
            R.id.spDocCity -> citySelected = arrayAdapterCities.getItem(position).toString()

        }

    }

    //Implemented methods of the interface AdapterView.OnItemSelectedListener (selected item)
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    //1st the app already has permission, 2. Permission was denied previously. 3. First time the app ask for permission
    private fun askForCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                takePhoto()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                //string for the message
                val permissionDeniedMessage = getString(R.string.ask_camera_permission)
                showMessage(permissionDeniedMessage)

            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), PERMISSION_CAMARA)
            }

        }
    }

    private fun askForFilesPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    == PackageManager.PERMISSION_GRANTED -> {
                uploadPhoto()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                //string for the message
                val permissionDeniedMessage = getString(R.string.ask_camera_permission)
                showMessage(permissionDeniedMessage)

            }
            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSION_EXTERNAL_STORAGE)
            }

        }


    }

    //allows to evaluate the result of the permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMARA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto()
                }
            }

            PERMISSION_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    uploadPhoto()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }

    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMARA_REQUEST_CODE)
    }

    private fun uploadPhoto() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes =
                arrayOf("images/jpeg", "images/jpg", "images/png") //only allows this format
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, IMAGE_REQUEST_CODE)
        }

    }


    //Manages the result of the photo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMARA_REQUEST_CODE -> {
                if (resultCode != Activity.RESULT_OK) {
                    //String for the message
                    val photoNotTaken = getString(R.string.activity_result_msg)
                    showMessage(photoNotTaken)
                } else {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    imageTakenBase64 = convertBitmapToBase64(bitmap)


                    // binding.ivTakePhotoDocs.setImageBitmap(bitmap) To show the image in a view
                }
            }
            IMAGE_REQUEST_CODE -> {
                if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {

                    //binding.ivTakePhotoDocs.setImageURI(data?.data) //The result of the pick up is here
                    //val bitmap = data?.extras?.get("data") as Bitmap
                    val bitmap = convertUriToBitmap(data?.data)
                    imageTakenBase64 = convertBitmapToBase64(bitmap)


                    // imageTakenBase64 = convertBitmapToBase64(bitmap)
                    //showMessage(imageTakenBase64)

                } else {
                    //String for message
                    val imgNotUploaded = getString(R.string.acivity_msg2)
                    showMessage(imgNotUploaded)
                }
            }
        }
    }


    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val image = stream.toByteArray()
        return android.util.Base64.encodeToString(image, android.util.Base64.DEFAULT)
    }

    fun convertUriToBitmap(uri: Uri?): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri!!)
            ImageDecoder.decodeBitmap(source)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun getInformationForPosting(): DocItemsPost {
        return DocItemsPost(
            currentDate,
            typeDocsSelected,
            binding.etIDNumberSendDocs.text.toString().trim(),
            binding.etNamesSendDocs.text.toString().trim(),
            binding.etLastNameSendDocs.text.toString().trim(),
            citySelected,
            binding.etEmailSendDocs.text.toString().trim(),
            binding.etDocTypeSendDocs.text.toString().trim(),
            imageTakenBase64

        )
    }


    private fun validateUsersDataForPost(): Boolean {
        if (getInformationForPosting().TipoId == "Tipo de documento" ||
            getInformationForPosting().Identificacion == "" ||
            getInformationForPosting().Nombre == "" ||
            getInformationForPosting().Apellido == "" ||
            getInformationForPosting().Correo == "" ||
            getInformationForPosting().Ciudad == "Ciudad" ||
            getInformationForPosting().TipoAdjunto == "" ||
            getInformationForPosting().Adjunto == "" ||
            getInformationForPosting().TipoAdjunto == ""
        ) {
            return false
        }
        return true
    }

    //Creates the menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //navigate to each option in the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sendDocsMenu -> {
                //string for the message
                val alreadySending = getString(R.string.already_sending)
                showMessage(alreadySending)
                true
            }
            R.id.seeDocsMenu -> {
                view?.findNavController()?.navigate(
                    SendDocsFragmentDirections.actionSendDocsFragmentToSeeDocsFragment(
                        arguments?.getString(
                            "user_email"
                        )
                    )
                )
                true
            }
            R.id.officesMenu -> {
                view?.findNavController()?.navigate(
                    SendDocsFragmentDirections.actionSendDocsFragmentToOfficesFragment(
                        arguments?.getString(
                            "user_email"
                        )
                    )
                )
                true
            }

            R.id.darkModeMenu -> {

                true
            }

            R.id.languageMenu -> {
                val getStringLanguage = getString(R.string.English_language)

                when (getStringLanguage) {
                    "Idioma Inglés" -> loadLocateEnglish()
                    "French Language" -> loadLocateFrench()
                    "Langue espagnole" -> loadLocateSpanish()
                }

                navigateFragmentItself()

                true
            }

            R.id.logoutMenu -> {
                view?.findNavController()?.navigate(R.id.action_sendDocsFragment_to_loginFragment)
                true
            }

            else -> {

                super.onOptionsItemSelected(item)
            }
        }
    }

    //save the preferences
    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, requireContext().resources.displayMetrics)
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPrefs.edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }
    //Write the preferences
    private fun loadLocateSpanish() {

        val sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            setLocate(language)
        }

    }

    private fun loadLocateEnglish() {

        val sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "en")
        if (language != null) {
            setLocate(language)
        }
    }

    private fun loadLocateFrench() {

        val sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "fr")
        if (language != null) {
            setLocate(language)
        }
    }
    //Upload the fragment to see the language changed
    private fun navigateFragmentItself() {
        view?.findNavController()
            ?.navigate(
                SendDocsFragmentDirections.actionSendDocsFragmentSelf(
                    arguments?.getString("user_email")
                )
            )
    }


}