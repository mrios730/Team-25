/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById('page-title').innerText = 'User Page: ' + parameterUsername;
  document.title = parameterUsername + ' - User Page';
}

/**
 * Shows the message form if the user is logged in and viewing their own page.
 */
function showMessageFormIfLoggedIn() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn) {
          document.getElementById('about-me-form').classList.remove('hidden');
          if (loginStatus.username == parameterUsername) {
            const messageForm = document.getElementById('message-form');
            fetchImageUploadUrlAndShowForm();
            messageForm.classList.remove('hidden');
            const recipientInput = document.getElementById('recipient-input');
            recipientInput.value = parameterUsername;
            if (parameterUsername !== '')
              recipientInput.parentElement.classList.add('is-dirty');
            componentHandler.upgradeElement(recipientInput);
          } else {
            // Looking at another user's page. Hide the 'Update' button and lock the about me form.
            document.getElementById('about-me-update-button-div').style.display = 'none';
            document.getElementById('about-me').disabled = true;
            document.getElementById('message-compose-card').style.display = 'none';
          }
        }
      });
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  const url = '/messages?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((messages) => {
        const messagesContainer = document.getElementById('message-container');
        if (messages.length == 0) {
          messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
        } else {
          messagesContainer.innerHTML = '';
        }
        messages.forEach((message) => {
          const messageDiv = buildMessageDiv(message);
          messagesContainer.appendChild(messageDiv);
        });
      });
}

/** Fetches about me and displays onto page. */
function fetchAboutMe() {
  const url = '/about?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.text();
      })
      .then((aboutMe) => {
        aboutMe = aboutMe.trim();
        const aboutMeContainer = document.getElementById('about-me');
        if (aboutMe !== '') {
          aboutMeContainer.value = aboutMe;
          aboutMeContainer.parentElement.classList.add('is-dirty');
        } else if (document.getElementById('about-me').disabled) {
          // No "about me" to display, and the form is disabled, so don't show the card at all.
          document.getElementById('about-me-card').style.display = 'none';
        }
        componentHandler.upgradeElement(aboutMeContainer);
      });
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(document.createTextNode(
      message.user + ' - ' + new Date(message.timestamp)));

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  if (message.imageUrl) {
    bodyDiv.innerHTML += '<br/>';
    bodyDiv.innerHTML += '<img src="' + message.imageUrl + '" />';
  }

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}

function fetchImageUploadUrlAndShowForm() {
  fetch('/image-upload-url')
    .then((response) => {
      return response.text();
    })
    .then((imageUploadUrl) => {
      const messageForm = document.getElementById('message-form');
      messageForm.action = imageUploadUrl;
    });
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  setPageTitle();
  showMessageFormIfLoggedIn();
  fetchMessages();
  fetchAboutMe();
  ClassicEditor.create( document.getElementById('message-input') );
}
