// Global variables
var failedToLoad = false;
var chat = [];

// Start collecting data
(function(){

// Local functions/variables
// Function to read from a JSON file
var prevPercent = 0;
var totalPercent = 0;
var currentPercent = 0;
function loadJSON(path, success, error) {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if (xhr.readyState === XMLHttpRequest.DONE) {
			if (xhr.status === 200) {
				if (typeof success === "function") {
					success(JSON.parse(xhr.responseText));
				}
				prevPercent += 100 / (lastChatFile + 2);
				currentPercent = 0;
				if(typeof loadingProgress === "function") {
					loadingProgress(prevPercent);
				}
			} else {
				if (typeof error === "function") {
					error(xhr);
				} else {
					failedToLoad = true;
					if(typeof loadingFailedCheck === "function") {
						loadingFailedCheck();
					}
				}
			}
		}
	};
	xhr.onprogress = function(e) {
		if(!e.lengthComputable) return;
		if(!lastChatFile) return;
		currentPercent = (e.loaded / e.total) * 100;
		totalPercent = prevPercent + currentPercent / (lastChatFile + 2);
		if(typeof loadingProgress === "function") {
			loadingProgress(totalPercent);
		}
	};
	xhr.open("GET", path, true);
	xhr.send();
}
var lastChatFile;

// Load the value of lastChatFile first
loadJSON("data/lastChatFile.txt?v=" + Date.now(), function(data) {
	lastChatFile = data;
	loadChatData(lastChatFile);
});

// Load all chat data afterwards
function loadChatData(i) {
	// Base case (all data already loaded)
	if(i < 0) { 
		if(loaded) loaded();
		return;
	}

	// Special case for first (newest) JSON file to prevent caching
	var suffix = '';
	if(i == lastChatFile) suffix = '?v=' + Date.now();

	// Load this chat data file
	loadJSON("data/chat" +i+ ".json" +suffix, function(data) {
		chat.push.apply(chat, data);

		// Recurse to load next chat data file
		i--;
		loadChatData(i);
	});
	
}


})(); // THE END

