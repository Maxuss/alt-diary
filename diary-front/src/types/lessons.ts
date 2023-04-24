import { UUID } from "crypto";

export interface Lesson {
    id: number;
    beginTime: string;
    endTime: string;
    nextBreakDuration: number;
    subject: Subject;
    marks: Mark[];
    homework: Homework;
}

export interface Mark {
    id: number;
    value: number;
    index: number;
    message: string;
    kind: MarkKind;
    teacherId: UUID;
    studentId: UUID;
}

export interface Homework {
    id: number;
    none: boolean;
    summary: string;
    teacherId: UUID;
    attachments: number[];
}

export enum MarkKind {
    GENERIC,
    EXAM,
    POINT
}

export interface Subject {
    id: number;
    name: string;
    teachers: UUID[];
}