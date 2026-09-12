const constraintRecord = {audio: true};
let audioRecorder;
let permittedStream;
let recordButton = document.querySelector("#recordButton");
let stopRecordButton = document.querySelector("#stopRecord");
let recordingStatus = document.querySelector("#recordingStatus");
let contentResponse = document.querySelector("#contentResponse");
let errorMessage = document.querySelector("#errorMessage")

let chunks = [];
recordButton.addEventListener("click", startRecord)
stopRecordButton.addEventListener("click", stopRecord)

function showError(message){
	errorMessage.style.display = "block";
	errorMessage.textContent = message;
	setTimeout(()=> {errorMessage.style.display = "none";}, 3000);
}
async function startRecord(){
	
	chunks = []; 
	try{
		permittedStream = await navigator.mediaDevices.getUserMedia(constraintRecord);
	}
	catch(error){
		
		
		if (error.name == "NotFoundError"){
			 showError("Microphone not found");
			
		}
		else if (error.name == "NotAllowedError" || error.name =="PermissionDeniedError"){
			
			showError("Microphone permission was denied");
		}
		else {
			showError("An error occurred. Could not process the recording.");
		}
		
		return;
	}
	audioRecorder = new MediaRecorder(permittedStream);
	audioRecorder.ondataavailable = (event) => { chunks.push(event.data)};
	audioRecorder.start();
	recordingStatus.style.display = "block";
	recordingStatus.textContent = "Recording started";
	
	console.log("Starting record");	
	
	recordButton.disabled = true;
	stopRecordButton.disabled = false;
		
	audioRecorder.onstop = async ()=> {
		try{
			const blobAudio = new Blob(chunks, {type: "audio/webm"});
						const url = URL.createObjectURL(blobAudio);
							
						console.log(url);
						
						const response = await uploadAudio(blobAudio);
						
						contentResponse.textContent = response.text;
		}
			catch(error){
				console.log("Upload failed:", error);
				showError("Could not upload the recording. Please try again.");
			}
		};
	
}

async function stopRecord(){
	try{
		recordingStatus.textContent = "Recording stopped";
			console.log("Stop record");
			audioRecorder.stop();
			
			for (const track of permittedStream.getTracks()){
				track.stop();
			}
	}
	catch(error){
			console.error("Error stopping recording:", error);
			showError("An error occurred while stopping the recording.");
		}
	finally{
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
			const responseBody = await postResponse.text();
			throw new Error(responseBody);
		}
		
		return postResponse.json();
	}
	catch(error){
		console.log("Error uploading audio:", error);
		throw error;
	}
	
		
	}

