package com.thekuzea.experimental.domain.mapper;

/**
 * RES - Resource
 * ENT - Entity
 */
public interface Mapper<RES, ENT> {

    ENT mapToModel(RES resource);

    RES mapToDto(ENT entity);
}
