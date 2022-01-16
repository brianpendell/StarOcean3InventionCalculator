import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IDiscipline } from 'app/shared/model/discipline.model';
import { getEntities as getDisciplines } from 'app/entities/discipline/discipline.reducer';
import { getEntity, updateEntity, createEntity, reset } from './invention.reducer';
import { IInvention } from 'app/shared/model/invention.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const InventionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const disciplines = useAppSelector(state => state.discipline.entities);
  const inventionEntity = useAppSelector(state => state.invention.entity);
  const loading = useAppSelector(state => state.invention.loading);
  const updating = useAppSelector(state => state.invention.updating);
  const updateSuccess = useAppSelector(state => state.invention.updateSuccess);

  const handleClose = () => {
    props.history.push('/invention');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getDisciplines({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...inventionEntity,
      ...values,
      discipline: disciplines.find(it => it.id.toString() === values.disciplineId.toString()),
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
          ...inventionEntity,
          disciplineId: inventionEntity?.discipline?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="so3InventionCalculatorApp.invention.home.createOrEditLabel" data-cy="InventionCreateUpdateHeading">
            <Translate contentKey="so3InventionCalculatorApp.invention.home.createOrEditLabel">Create or edit a Invention</Translate>
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
                  id="invention-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('so3InventionCalculatorApp.invention.name')}
                id="invention-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('so3InventionCalculatorApp.invention.cost')}
                id="invention-cost"
                name="cost"
                data-cy="cost"
                type="text"
              />
              <ValidatedField
                label={translate('so3InventionCalculatorApp.invention.difficulty')}
                id="invention-difficulty"
                name="difficulty"
                data-cy="difficulty"
                type="text"
              />
              <ValidatedField
                id="invention-discipline"
                name="disciplineId"
                data-cy="discipline"
                label={translate('so3InventionCalculatorApp.invention.discipline')}
                type="select"
              >
                <option value="" key="0" />
                {disciplines
                  ? disciplines.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/invention" replace color="info">
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

export default InventionUpdate;
