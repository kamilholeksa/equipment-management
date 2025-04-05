export interface UserModel {
  id: number;
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  phoneNumber: string;
  active: boolean;
  roles: string[];
  createdBy: string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
}

export interface UserSaveModel {
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  confirmPassword: string;
  email: string;
  phoneNumber: string;
  active: boolean;
  roles: string[];
}
