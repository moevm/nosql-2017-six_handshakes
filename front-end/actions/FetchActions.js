export function fetchRequest() {
    return {
        type: 'FETCH_REQUEST'
    }
}

export function fetchSuccess() {
    return {
        type: 'FETCH_SUCCESS'
    }
}

export function fetchFailure() {
    return {
        type: 'FETCH_FAILURE'
    }
}