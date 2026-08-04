package com.tamojit.contentservice.model;

/**
 * Tracks video processing lifecycle
 * Pipeline Flow:
 * PENDING -> UPLOADED -> ENCODING -> ENCODED -> READY (success)
 * if not READY -> FAILED (failure)
 */
public enum VideoStatus {
    PENDING,
    UPLOADED,
    ENCODING,
    ENCODED,
    READY,
    FAILED
}
