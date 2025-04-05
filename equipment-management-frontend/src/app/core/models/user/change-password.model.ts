export interface ChangeCurrentUserPasswordModel {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

export interface ChangePasswordModel {
  newPassword: string;
  confirmPassword: string;
}
