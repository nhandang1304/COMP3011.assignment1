
const constraintRecord = {audio: true}; // Audio recording configuration
let audioRecorder; // Stores the MediaRecorder instance
let permittedStream; // Stores the microphone stream

// Get references to the HTML elements
let recordButton = document.querySelector("#recordButton");
let stopRecordButton = document.querySelector("#stopRecord");
let recordingStatus = document.querySelector("#recordingStatus");
let instruction = document.querySelector("#instruction");
let contentResponse = document.querySelector("#contentResponse");
let errorMessage = document.querySelector("#errorMessage");
let recordingIndicator = document.querySelector("#recordingIndicator");

//Bootstrap tooltips
const tooltipTriggerList = document.querySelectorAll(
    '[data-bs-toggle="tooltip"]'
);

tooltipTriggerList.forEach(
    tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl)
);

// The audio data storage
let chunks = [];

// Add event listeners to the start and stop buttons
recordButton.addEventListener("click", startRecord)
stopRecordButton.addEventListener("click", stopRecord)


// Function to display an error message for a limited amount of time
function showError(element, message){
	element.style.display = "block";
	element.textContent = message;
	setTimeout(()=> {element.style.display = "none";}, 5000);
}

// Function to Start recording audio from the user's microphone
async function startRecord(){
	
	chunks = []; // Clear audio data from any previous recording
	try{
		permittedStream = await navigator.mediaDevices.getUserMedia(constraintRecord); // Ask user permission to access the user's microphone
	}
	catch(error){
		
		// No microphone is available
		if (error.name == "NotFoundError"){
			 showError(errorMessage, "Microphone not found");
			
		}
		// Deny microphone permission
		else if (error.name == "NotAllowedError" || error.name =="PermissionDeniedError"){
			
			showError(errorMessage, "Microphone permission was denied");
		}
		// Any other microphone access
		else {
			showError(errorMessage, "An error occurred. Could not process the recording.");
		}
		
		return;
	}
	
	// MediaRecorder to record the microphone stream
	audioRecorder = new MediaRecorder(permittedStream);
	
	audioRecorder.ondataavailable = (event) => { chunks.push(event.data)};   // Store each piece of recorded audio in the chunks
	audioRecorder.start();
	
	// Updating messages to show starting record
	recordingIndicator.style.display = 'block';
	recordingStatus.style.display = "block";
	recordingStatus.textContent = "Recording started";
	instruction.textContent = "Click the mute icon to stop recording.";
	console.log("Starting record");	
	
	recordButton.disabled = true; //Prevent starting another recording while recording 
	stopRecordButton.disabled = false;  //Allow to stop recording
		
	
	// Handle the audio after recording has stopped
	audioRecorder.onstop = async ()=> {
		try{
			// Creating Blob to combine audio chunks
			const blobAudio = new Blob(chunks, {type: "audio/webm"});
						recordingStatus.textContent = "Uploading and transcribing...";
						const response = await uploadAudio(blobAudio);	// Upload the audio and wait for the transcription response				
						contentResponse.textContent = response.text;	// Display transcription content
						recordingStatus.textContent = "Transcription completed";
		}
		// Display errors if uploading fails
			catch(error){
				console.log(error.message);
				showError(recordingStatus, "Transcription fails.");
				showError(errorMessage, "Could not upload the recording. Please try again.");
			}
		};
	
}

// Function to stop the recording
async function stopRecord(){
	try{
		recordingStatus.textContent = "Recording stopped";
		recordingIndicator.style.display = 'none';
			console.log("Stop record");
			audioRecorder.stop();
			
			// Release microphone access by stoping all tracks
			for (const track of permittedStream.getTracks()){
				track.stop();
			}
	}
	catch(error){
		// Show errors when recording has not been started 
			console.log("Error stopping recording");
			showError(errorMessage, "Please start recording before stopping.");
		}
	finally{
		// Reseting the interface for the next recording
		instruction.textContent = "Click the microphone and start speaking";
		recordButton.disabled = false;
		stopRecordButton.disabled = true;
	}
	
}

// Function to upload audio to API 
async function uploadAudio(blobAudio){
	try{
		const dataForm = new FormData();  // Create a FormData object for the multipart request
		dataForm.append("audioRecord", blobAudio, "audioFile.webm");
		
		// Send the audio to the backend API
		const postResponse = await fetch("/api/speech", { method: "POST", body: dataForm});
		
		// Returned an unsuccessful response
		if (!postResponse.ok){
			throw new Error("Error uploading audio");
		}
		
		return postResponse.json(); // Convert the JSON response into a JavaScript object
	}
	catch(error){
		
		throw error; // Pass the error back to the caller
	}
	
		
	}

