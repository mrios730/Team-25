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

/**
 * Adds the navigation bar links based on whether the user is logged in.
 */
function addLoginOrLogoutLinkToNavigation(currentTab = '') {
  const navigationElement = document.getElementById('navigation');
  navigationElement.classList.add('message-header');
  if (!navigationElement) {
    console.warn('Navigation element not found!');
    return;
  }

  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        navigationElement.appendChild(createLink(
            '/', 'Home', currentTab));
        if (loginStatus.isLoggedIn) {
          navigationElement.appendChild(createLink(
              '/user-page.html?user=' + loginStatus.username, 'Your Page', currentTab));
          navigationElement.appendChild(
              createLink('/stats.html', 'Site Stats', currentTab));
          navigationElement.appendChild(
              createLink('/logout', 'Log Out', currentTab));
        } else {
          navigationElement.appendChild(
              createLink('/login', 'Log In', currentTab));
        }
      });
}

/**
 * Creates an anchor element.
 * @param {string} url
 * @param {string} text
 * @param {string} currentTab
 * @return {Element} Anchor element
 */
function createLink(url, text, currentTab) {
  const linkElement = document.createElement('a');
  linkElement.appendChild(document.createTextNode(text));
  linkElement.href = url;
  linkElement.classList.add('mdl-layout__tab');
  if (text === currentTab) {
    linkElement.classList.add('is-active');
  }
  componentHandler.upgradeElement(linkElement);

  // TODO: Remove this once all pages are switched to MDL style.
  if (!currentTab) {
    const listItemElement = document.createElement('li');
    listItemElement.appendChild(linkElement);
    return listItemElement;
  }

  return linkElement;
}
