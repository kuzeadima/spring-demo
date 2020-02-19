package com.thekuzea.experimental.mapper;

/**
 * RES - Resource
 * ENT - Entity
 */
public interface Mapper<RES, ENT> {

    ENT mapToModel(RES resource);

    RES mapToDto(ENT entity);
}
