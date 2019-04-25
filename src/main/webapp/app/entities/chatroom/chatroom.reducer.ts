import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IChatroom, defaultValue } from 'app/shared/model/chatroom.model';

export const ACTION_TYPES = {
  FETCH_CHATROOM_LIST: 'chatroom/FETCH_CHATROOM_LIST',
  FETCH_CHATROOM: 'chatroom/FETCH_CHATROOM',
  CREATE_CHATROOM: 'chatroom/CREATE_CHATROOM',
  UPDATE_CHATROOM: 'chatroom/UPDATE_CHATROOM',
  DELETE_CHATROOM: 'chatroom/DELETE_CHATROOM',
  RESET: 'chatroom/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IChatroom>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ChatroomState = Readonly<typeof initialState>;

// Reducer

export default (state: ChatroomState = initialState, action): ChatroomState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CHATROOM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CHATROOM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CHATROOM):
    case REQUEST(ACTION_TYPES.UPDATE_CHATROOM):
    case REQUEST(ACTION_TYPES.DELETE_CHATROOM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CHATROOM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CHATROOM):
    case FAILURE(ACTION_TYPES.CREATE_CHATROOM):
    case FAILURE(ACTION_TYPES.UPDATE_CHATROOM):
    case FAILURE(ACTION_TYPES.DELETE_CHATROOM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHATROOM_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHATROOM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CHATROOM):
    case SUCCESS(ACTION_TYPES.UPDATE_CHATROOM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CHATROOM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/chatrooms';

// Actions

export const getEntities: ICrudGetAllAction<IChatroom> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CHATROOM_LIST,
    payload: axios.get<IChatroom>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IChatroom> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CHATROOM,
    payload: axios.get<IChatroom>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IChatroom> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CHATROOM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IChatroom> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CHATROOM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IChatroom> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CHATROOM,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
