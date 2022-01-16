import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IInventor } from 'app/shared/model/inventor.model';
import { getEntities as getInventors } from 'app/entities/inventor/inventor.reducer';
import { IDiscipline } from 'app/shared/model/discipline.model';
import { getEntities as getDisciplines } from 'app/entities/discipline/discipline.reducer';
import { getEntity, updateEntity, createEntity, reset } from './discipline-skill.reducer';
import { IDisciplineSkill } from 'app/shared/model/discipline-skill.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DisciplineSkillUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const inventors = useAppSelector(state => state.inventor.entities);
  const disciplines = useAppSelector(state => state.discipline.entities);
  const disciplineSkillEntity = useAppSelector(state => state.disciplineSkill.entity);
  const loading = useAppSelector(state => state.disciplineSkill.loading);
  const updating = useAppSelector(state => state.disciplineSkill.updating);
  const updateSuccess = useAppSelector(state => state.disciplineSkill.updateSuccess);

  const handleClose = () => {
    props.history.push('/discipline-skill');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getInventors({}));
    dispatch(getDisciplines({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...disciplineSkillEntity,
      ...values,
      inventor: inventors.find(it => it.id.toString() === values.inventorId.toString()),
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
          ...disciplineSkillEntity,
          inventorId: disciplineSkillEntity?.inventor?.id,
          disciplineId: disciplineSkillEntity?.discipline?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="so3InventionCalculatorApp.disciplineSkill.home.createOrEditLabel" data-cy="DisciplineSkillCreateUpdateHeading">
            <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.home.createOrEditLabel">
              Create or edit a DisciplineSkill
            </Translate>
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
                  id="discipline-skill-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('so3InventionCalculatorApp.disciplineSkill.skillModifier')}
                id="discipline-skill-skillModifier"
                name="skillModifier"
                data-cy="skillModifier"
                type="text"
              />
              <ValidatedField
                id="discipline-skill-inventor"
                name="inventorId"
                data-cy="inventor"
                label={translate('so3InventionCalculatorApp.disciplineSkill.inventor')}
                type="select"
              >
                <option value="" key="0" />
                {inventors
                  ? inventors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="discipline-skill-discipline"
                name="disciplineId"
                data-cy="discipline"
                label={translate('so3InventionCalculatorApp.disciplineSkill.discipline')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/discipline-skill" replace color="info">
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

export default DisciplineSkillUpdate;
