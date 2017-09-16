export let RequestParamsFactory = {
    GET: () => {
        return {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Access-Control-Allow-Origin': '*'
            }
        }
    }
};

export function fetchGet(url, dispatch, onSuccess, onFailure) {
    return fetchWrapper(url, RequestParamsFactory.GET(), dispatch, onSuccess, onFailure);
}

//TODO use isomorphic-fetch or fetch-polyfill
function fetchWrapper(url, params, dispatch, onSuccess, onFailure) {
    return fetch(url, params)
        .then(
            response => {
                console.log('RESPONSE', response);
                if(response.status === 200) {
                    return response.json();
                }
                else {
                    let errorMessage = `\nCode ${response.status}: ${response.statusText}\n${response.url}`;
                    throw new Error(errorMessage);
                }
            }
        )
        .then(json => dispatch(onSuccess(json)))
        .catch(error => dispatch(onFailure(`An error occurred. ${error.message}`)));
}

