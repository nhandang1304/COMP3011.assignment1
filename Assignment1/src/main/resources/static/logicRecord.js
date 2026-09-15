const constraintRecord = {audio: true};
let audioRecorder;
let permittedStream;
let recordButton = document.querySelector("#recordButton");
let stopRecordButton = document.querySelector("#stopRecord");
let recordingStatus = document.querySelector("#recordingStatus");
let instruction = document.querySelector("#instruction");
let contentResponse = document.querySelector("#contentResponse");
let errorMessage = document.querySelector("#errorMessage");
let recordingIndicator = document.querySelector("#recordingIndicator");

const tooltipTriggerList = document.querySelectorAll(
    '[data-bs-toggle="tooltip"]'
);

tooltipTriggerList.forEach(
    tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl)
);
let chunks = [];
recordButton.addEventListener("click", startRecord)
stopRecordButton.addEventListener("click", stopRecord)



function showError(element, message){
	element.style.display = "block";
	element.textContent = message;
	setTimeout(()=> {element.style.display = "none";}, 5000);
}
async function startRecord(){
	
	chunks = []; 
	try{
		permittedStream = await navigator.mediaDevices.getUserMedia(constraintRecord);
	}
	catch(error){
		
		
		if (error.name == "NotFoundError"){
			 showError(errorMessage, "Microphone not found");
			
		}
		else if (error.name == "NotAllowedError" || error.name =="PermissionDeniedError"){
			
			showError(errorMessage, "Microphone permission was denied");
		}
		else {
			showError(errorMessage, "An error occurred. Could not process the recording.");
		}
		
		return;
	}
	audioRecorder = new MediaRecorder(permittedStream);
	
	audioRecorder.ondataavailable = (event) => { chunks.push(event.data)};
	audioRecorder.start();
	
	recordingIndicator.style.display = 'block';
	recordingStatus.style.display = "block";
	recordingStatus.textContent = "Recording started";
	instruction.textContent = "Click the mute icon to stop recording.";
	console.log("Starting record");	
	
	recordButton.disabled = true;
	stopRecordButton.disabled = false;
		
	audioRecorder.onstop = async ()=> {
		try{
			
			const blobAudio = new Blob(chunks, {type: "audio/webm"});
						recordingStatus.textContent = "Uploading and transcribing...";
						const response = await uploadAudio(blobAudio);						
						contentResponse.textContent = response.text;
						recordingStatus.textContent = "Transcription completed";
		}
			catch(error){
				console.log(error.message);
				showError(recordingStatus, "Transcription fails.");
				showError(errorMessage, "Could not upload the recording. Please try again.");
			}
		};
	
}

async function stopRecord(){
	try{
		recordingStatus.textContent = "Recording stopped";
		recordingIndicator.style.display = 'none';
			console.log("Stop record");
			audioRecorder.stop();
			
			for (const track of permittedStream.getTracks()){
				track.stop();
			}
	}
	catch(error){
			console.log("Error stopping recording");
			showError(errorMessage, "Please start recording before stopping.");
		}
	finally{
		instruction.textContent = "Click the microphone and start speaking";
		recordButton.disabled = false;
		stopRecordButton.disabled = true;
	}
	
}

async function uploadAudio(blobAudio){
	try{
		const dataForm = new FormData();
		dataForm.append("audioRecord", blobAudio, "audioFile.webm");
		
		const postResponse = await fetch("/api/speech", { method: "POST", body: dataForm});
		
		
		if (!postResponse.ok){
			throw new Error("Error uploading audio");
		}
		
		return postResponse.json();
	}
	catch(error){
		
		throw error;
	}
	
		
	}

