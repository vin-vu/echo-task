type TaskProps = {
  description: string;
};

export default function Task({ description }: TaskProps) {
  return (
    <>
      <p>{description}</p>
    </>
  );
}
