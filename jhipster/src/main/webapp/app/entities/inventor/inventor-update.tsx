import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IInvention } from 'app/shared/model/invention.model';
import { getEntities as getInventions } from 'app/entities/invention/invention.reducer';
import { getEntity, updateEntity, createEntity, reset } from './inventor.reducer';
import { IInventor } from 'app/shared/model/inventor.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const InventorUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const inventions = useAppSelector(state => state.invention.entities);
  const inventorEntity = useAppSelector(state => state.inventor.entity);
  const loading = useAppSelector(state => state.inventor.loading);
  const updating = useAppSelector(state => state.inventor.updating);
  const updateSuccess = useAppSelector(state => state.inventor.updateSuccess);

  const handleClose = () => {
    props.history.push('/inventor');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getInventions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...inventorEntity,
      ...values,
      invention: inventions.find(it => it.id.toString() === values.inventionId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...inventorEntity,
          inventionId: inventorEntity?.invention?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="so3InventionCalculatorApp.inventor.home.createOrEditLabel" data-cy="InventorCreateUpdateHeading">
            <Translate contentKey="so3InventionCalculatorApp.inventor.home.createOrEditLabel">Create or edit a Inventor</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="inventor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('so3InventionCalculatorApp.inventor.name')}
                id="inventor-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('so3InventionCalculatorApp.inventor.timeModifer')}
                id="inventor-timeModifer"
                name="timeModifer"
                data-cy="timeModifer"
                type="text"
              />
              <ValidatedField
                label={translate('so3InventionCalculatorApp.inventor.costModifier')}
                id="inventor-costModifier"
                name="costModifier"
                data-cy="costModifier"
                type="text"
              />
              <ValidatedField
                id="inventor-invention"
                name="inventionId"
                data-cy="invention"
                label={translate('so3InventionCalculatorApp.inventor.invention')}
                type="select"
              >
                <option value="" key="0" />
                {inventions
                  ? inventions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/inventor" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default InventorUpdate;
