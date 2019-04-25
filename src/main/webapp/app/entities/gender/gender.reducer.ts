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

import { IGender, defaultValue } from 'app/shared/model/gender.model';

export const ACTION_TYPES = {
  FETCH_GENDER_LIST: 'gender/FETCH_GENDER_LIST',
  FETCH_GENDER: 'gender/FETCH_GENDER',
  CREATE_GENDER: 'gender/CREATE_GENDER',
  UPDATE_GENDER: 'gender/UPDATE_GENDER',
  DELETE_GENDER: 'gender/DELETE_GENDER',
  RESET: 'gender/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGender>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type GenderState = Readonly<typeof initialState>;

// Reducer

export default (state: GenderState = initialState, action): GenderState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GENDER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GENDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GENDER):
    case REQUEST(ACTION_TYPES.UPDATE_GENDER):
    case REQUEST(ACTION_TYPES.DELETE_GENDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GENDER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GENDER):
    case FAILURE(ACTION_TYPES.CREATE_GENDER):
    case FAILURE(ACTION_TYPES.UPDATE_GENDER):
    case FAILURE(ACTION_TYPES.DELETE_GENDER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GENDER_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_GENDER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GENDER):
    case SUCCESS(ACTION_TYPES.UPDATE_GENDER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GENDER):
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

const apiUrl = 'api/genders';

// Actions

export const getEntities: ICrudGetAllAction<IGender> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GENDER_LIST,
    payload: axios.get<IGender>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IGender> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GENDER,
    payload: axios.get<IGender>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGender> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GENDER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IGender> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GENDER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGender> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GENDER,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
