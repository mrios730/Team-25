// Fetch messages and add them to the page.
function fetchEvents(){
  const url = "/event-list";
  fetch(url).then((response) => {
    return response.json();
  }).then((events) => {
    const eventContainer = document.getElementById('event-container');
    if(events.length == 0){
     eventContainer.innerHTML = '<p>There are no events at this time. \
     Please check again later</p>';
    }
    else{
     eventContainer.innerHTML = '';  
    }
    events.forEach((event) => {  
     const eventDiv = buildEventDiv(event);
     eventContainer.appendChild(eventDiv);
    });
  });
}

function buildEventDiv(event){
 // For the event name
 const eventNameDiv = document.createElement('div');
 eventNameDiv.classList.add("left-align");
 eventNameDiv.appendChild(document.createTextNode(event.eventName));

 // Organizers name
 const organizerNamesDiv = document.createElement('div');
 organizerNamesDiv.classList.add("left-align");
 organizerNamesDiv.appendChild(document.createTextNode(event.organizerNames));
 
 // Will need some way to nicely format the date/time of the event later on
 const dateDiv = document.createElement('div');
 dateDiv.classList.add('right-align');
 dateDiv.appendChild(document.createTextNode(event.eventDate));
 
 //Time of event
 const timeDiv = document.createElement('div');
 timeDiv.classList.add('right-align');
 timeDiv.appendChild(document.createTextNode(event.eventTime));

 // Location of event
 const locationDiv = document.createElement('div');
 locationDiv.classList.add('right-align');
 locationDiv.appendChild(document.createTextNode(event.location));

 // Group as one thing
 const headerDiv = document.createElement('div');
 headerDiv.classList.add('message-header');
 headerDiv.appendChild(eventNameDiv);
 headerDiv.appendChild(organizerNamesDiv);
 headerDiv.appendChild(dateDiv);
 headerDiv.appendChild(timeDiv);
 headerDiv.appendChild(locationDiv);
 
 const bodyDiv = document.createElement('div');
 bodyDiv.classList.add('message-body');
 bodyDiv.appendChild(document.createTextNode(event.description));
 
 const messageDiv = document.createElement('div');
 messageDiv.classList.add("message-div");
 messageDiv.appendChild(headerDiv);
 messageDiv.appendChild(bodyDiv);
 
 return messageDiv;
}

// Fetch data and populate the UI of the page.
function buildUI(){
  fetchEvents();
}
