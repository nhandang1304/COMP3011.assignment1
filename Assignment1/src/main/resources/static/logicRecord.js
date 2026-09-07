const constraintRecord = {audio: true};
let audioRecorder;
let recordButton = document.querySelector("#recordButton");
let isRecording = false;
let chunks = [];
recordButton.addEventListener("click", startRecord()=>{
	if 
})

async function startRecord(){
	const permittedStream = await navigator.mediaDevices.getUserMedia(constraintRecord);
	audioRecorder = new MediaRecorder(permittedStream);
	audioRecorder.ondataavailable = (event) => { chunks.push(event.data)};
	audioRecorder.start();	
	
}

async function stopRecord(){
	audioRecorder.stop();
	audioRecorder.onstop = ()=> {
		const blobAudio = new Blob(chunks, {type: "audio/webm"});
	};
	const url = URL.createObjectURL(blobAudio);
	console.log("audioURL");
	
}
